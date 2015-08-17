package fr.philippefichet.eslogfx;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fxmisc.cssfx.CSSFX;
import org.fxmisc.cssfx.api.URIToPathConverter;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
//        System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
//        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
//        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");
//        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl.conn", "DEBUG");
//        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl.client", "DEBUG");
//        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.client", "DEBUG");
//        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "DEBUG");
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
        fxmlLoader.load();
        Parent root = fxmlLoader.getRoot();
        URIToPathConverter myConverter = new URIToPathConverter() {
            @Override
            public Path convert(String uri) {
                if (uri.startsWith("/styles")) {
                    String source = new File(".").getAbsolutePath() + 
                        File.separator + "src" + 
                        File.separator + "main" + 
                        File.separator + "resources" + 
                        uri.replace("/", File.separator);
                    return Paths.get(source);
                } else {
                    return Paths.get(uri);
                }
            }
        };
//        System.getProperties().setProperty("cssfx.log.level", "DEBUG");
        CSSFX.addConverter(myConverter).start();
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/dark.css");
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setTitle("ElasticSearch Log FX");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest((e) -> {
            Platform.exit();
        });
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
