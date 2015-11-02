package fr.philippefichet.eslogfx;

import fr.philippefichet.eslogfx.elasticsearch.Hit;
import fr.philippefichet.eslogfx.elasticsearch.Result;
import fr.philippefichet.eslogfx.elasticsearch.UniqHashMap;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

public class ElasticSearchController implements Initializable {

    @FXML
    private TableView<Map<String, String>> tableLogs;

    @FXML
    private Button getLogs;

    @FXML
    private Button getLogsInES;

    @FXML
    private Button localRemove;

    @FXML
    private Label schedulerLabel;

    @FXML
    private Slider schedulerSlider;

    @FXML
    private TextField filter;

    @FXML
    private TextField exclude;

    @FXML
    private ProgressBar downloadProgress;

    @FXML
    private Label downloadProgressText;

    @FXML
    private Label currentTask;

    SimpleObjectProperty<Duration> scheduleDuration = new SimpleObjectProperty<>(Duration.seconds(5));

    private Map<String, TableColumn<Map<String, String>, String>> columns = new HashMap<>();

    private String fieldLevel = "";
    // À mettre en config
    private String fieldMessage = "message";
    // Static
    private String fieldMessageComplete = "fieldMessageComplete";
    private Integer fieldMessageNumberLine = 1;

    // Level possible pour determiner le champs du level
    private List<String>levelAvailable = new ArrayList<>();

    private Comparator<Map<String, String>> comparatorRableLogs;

    private final ObservableList<Map<String, String>> logs = FXCollections.observableArrayList();
    private final ObservableList<Map<String, String>> logsFiltered = new FilteredList<>(logs);

    // Optimisation memoire pour les chaînes de caractére identique
    private final Map<String, List<String>> enumField = new HashMap<>();
    private final List<String> enumFieldExcluded = new ArrayList<>();
    private final Short enumFieldLimit = 50;

    /**
     * Vérifie si le log doit être affiché
     * @param log Log à vérifier
     * @return true si le log doit être afficher, false sinon
     */
    public boolean filterLogs(Map<String, String> log) {
        return log.get(fieldMessageComplete).contains(filter.getText()) && (!exclude.getText().isEmpty() && log.get(fieldMessageComplete).contains(exclude.getText()) == false || exclude.getText().isEmpty());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        levelAvailable.add("trace");
        levelAvailable.add("debug");
        levelAvailable.add("info");
        levelAvailable.add("warn");
        levelAvailable.add("warning");
        levelAvailable.add("notice");
        levelAvailable.add("error");
        levelAvailable.add("err");
        levelAvailable.add("severe");
        levelAvailable.add("fatal");
        levelAvailable.add("alert");
        levelAvailable.add("crit");
        levelAvailable.add("emerg");
        
        downloadProgressText.textProperty().bind(downloadProgress.progressProperty().multiply(100).asString("%.2f").concat("%"));
        tableLogs.setOnMouseClicked((event) -> {
            if(event.getClickCount() > 1) {
                Map<String, String> row = tableLogs.getSelectionModel().getSelectedItem();
                Pane root = new DetailsLogs(row.get(fieldMessageComplete));
                Scene scene = new Scene(root, 800, 600);
                scene.getStylesheets().add("/styles/Styles.css");
                scene.getStylesheets().add("/styles/dark.css");
                Stage stage = new Stage();
                stage.setTitle("My New Stage Title");
                stage.setScene(scene);
                stage.show();
            }
        });
        filter.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                tableLogs.setItems(logs.filtered(this::filterLogs));
            }
        });
        exclude.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                tableLogs.setItems(logs.filtered(this::filterLogs));
            }
        });
        FilteredList<Map<String, String>> filtered = logs.filtered((Map<String, String> t) -> {
            // Future filtre pour les messages pour les niveau de logs
            return true;
        });
        tableLogs.setItems(filtered);

        Config config = Config.getInstance();
        if (config.getFieldMessage() != null) {
            fieldMessage = config.getFieldMessage();
        } else {
            config.setFieldMessage(fieldMessage);
        }
        if(config.getFieldMessageNumberLine() != null) {
            fieldMessageNumberLine = config.getFieldMessageNumberLine();
        }
        comparatorRableLogs = (Map<String, String> o1, Map<String, String> o2) -> o2.get(config.getFieldDate()).compareTo(o1.get(config.getFieldDate()));
        ElasticSearchLogService service;
        service = new ElasticSearchLogService(config);
        service.setOnSucceeded(value -> {
            Result result = ((Result)value.getSource().getValue());
            for (Hit hit : result.getHits().getHits()) {
                // Initialisation des colonnes
                if (columns.isEmpty()) {
                    ObservableList<TableColumn<Map<String, String>, ?>> tableColumns = tableLogs.getColumns();
                    hit.getSource().forEach((String k, String v) -> {
                        if (k.startsWith("_") == false && config.getFieldExclude().contains(k) == false) {
                            // Traitement des colonnes
                            TableColumn<Map<String, String>, String> c = new TableColumn<>(k);
                            columns.put(k, c);
                            c.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {
                                @Override
                                public ObservableValue<String> call(TableColumn.CellDataFeatures<Map<String, String>, String> param) {
                                    return new SimpleObjectProperty<String>(param.getValue().get(param.getTableColumn().getText()));
                                }
                            });
                            tableColumns.add(c);
                            if (levelAvailable.contains(v.toLowerCase())) {
                                fieldLevel = k;
                                config.setFieldLevel(fieldLevel);
                            }
                        }
                    });

                    columns.forEach((k,c) -> {
                        c.setCellFactory(new Callback<TableColumn<Map<String, String>, String>, TableCell<Map<String, String>, String>>() {
                            @Override
                            public TableCell<Map<String, String>, String> call(TableColumn<Map<String, String>, String> param) {
                                return new TableCellLog(fieldLevel);
                            }
                        });
                    });

                    if (fieldMessage != null) {
                        TableColumn<Map<String, String>, ?> messageColumn = null;
                        Iterator<TableColumn<Map<String, String>, ?>> iterator = tableLogs.getColumns().iterator();
                        while(iterator.hasNext()) {
                            TableColumn<Map<String, String>, ?> next = iterator.next();
                            if(next.textProperty().get().equals(fieldMessage)) {
                                messageColumn = next;
                                iterator.remove();
                                break;
                            }
                        }
                        if (messageColumn != null) {
                            tableLogs.getColumns().add(messageColumn);
                        }
                    }
                }
                Map<String, String> row = new UniqHashMap();
                row.put("_id", hit.getId());
                if (logs.contains(row)) {
                    continue;
                }
                hit.getSource().forEach((String k, String v) -> {
                    if (k.startsWith("_") == false && config.getFieldExclude().contains(k) == false) {
                        // Si le champs n'est pas exclus des enums
                        if (!enumFieldExcluded.contains(v)) {
                            List<String> enumValues = enumField.get(k);
                            if (enumValues == null) {
                                enumValues = new ArrayList<>();
                            }
                            int indexEnum = enumValues.indexOf(v);
                            if (indexEnum == -1) {
                                // Dépassement de la limite des enums
                                if (enumValues.size() > enumFieldLimit) {
                                    enumFieldExcluded.add(k);
                                    enumValues.clear();
                                    enumField.remove(k);
                                } else {
                                    enumValues.add(v);
                                }
                            } else {
                                v = enumValues.get(indexEnum);
                            }
                        }
                        row.put(k, v);
                    }
                });
                if (fieldMessage != null) {
                    String message = row.get(fieldMessage);
                    if (message != null) {
                        row.put(fieldMessageComplete, message);
                        String[] split = message.split("\n");
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < Math.min(fieldMessageNumberLine, split.length); i++) {
                            sb.append(split[i]).append("\n");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        row.put(fieldMessage, sb.toString());
                    }
                }
                logs.add(row);
            }
            currentTask.textProperty().unbind();
            downloadProgress.progressProperty().unbind();
            downloadProgress.setProgress(1.0d);
            currentTask.setText("Tri des logs");
            FXCollections.sort(logs, comparatorRableLogs);
            currentTask.setText("Terminer");
            getLogs.setDisable(false);
            getLogsInES.setDisable(false);
            scheduleDuration.set(Duration.seconds(schedulerSlider.valueProperty().intValue()));
        });
        service.setOnFailed(value -> {
            value.getSource().getException().printStackTrace();
            getLogs.setDisable(false);
            getLogsInES.setDisable(false);
            currentTask.textProperty().unbind();
            service.restart();
            currentTask.setText("Erreur : " + value.getSource().getException().getLocalizedMessage());
        });
        service.setOnRunning((e) -> {
            downloadProgress.progressProperty().bind(service.progressProperty());
            currentTask.textProperty().bind(service.messageProperty());
            getLogs.setDisable(true);
        });
        getLogs.setOnAction((event) -> {
            getLogs.setDisable(true);
            service.cancel();
            scheduleDuration.set(Duration.seconds(0));
            service.restart();
        });
        getLogsInES.setOnAction((event) -> {
            getLogsInES.setDisable(true);
            getLogs.setDisable(true);
            service.cancel();
            scheduleDuration.set(Duration.seconds(0));
            service.nextQueryWithFilter(filter.getText());
            service.restart();
        });
        
        localRemove.setOnAction(e -> {
            logs.clear();
        });

        schedulerLabel.textProperty().bind(schedulerSlider.valueProperty().asString("%.0f s."));
        schedulerSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            service.cancel();
            scheduleDuration.set(Duration.seconds(newValue.intValue()));
            service.restart();
        });

        service.periodProperty().bind(scheduleDuration);
        service.start();
    }
}
