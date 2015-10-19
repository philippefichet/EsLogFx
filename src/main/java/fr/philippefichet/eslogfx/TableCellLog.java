/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.philippefichet.eslogfx;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;

/**
 *
 * @author philippefichet
 */
public class TableCellLog extends TableCell<Map<String, String>, String>{

    private static Map<String, String> levels = new HashMap<>();
    static {
        levels.put("trace", "trace");
        levels.put("debug", "debug");
        levels.put("info", "info");
        levels.put("warn", "warn");
        levels.put("warning", "warn");
        levels.put("notice", "warn");
        levels.put("error", "error");
        levels.put("err", "error");
        levels.put("fatal", "fatal");
        levels.put("severe", "fatal");
        levels.put("alert", "fatal");
        levels.put("crit", "fatal");
        levels.put("emerg", "fatal");
    }

    private String fieldLevel = null;

    private String customStyle = "";

    public TableCellLog(String fieldLevel) {
        this.fieldLevel = fieldLevel;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        if (empty) {
            setText(null);
            getStyleClass().remove(customStyle);
            customStyle = "";
        } else {
            setText(item);
            TableRow tableRow = getTableRow();
            if (tableRow != null) {
                Map<String, String> row = (Map<String, String>)tableRow.getItem();
                if (row != null) {
                    String level = row.get(fieldLevel);
                    if (level != null) {
                        String styleClass = levels.get(level.toLowerCase());
                        if (styleClass == null) {
                            System.err.println(level.toLowerCase() + " non disponible");
                        } else {
                            if (customStyle.equals(styleClass) == false) {
                                getStyleClass().remove(customStyle);
                                getStyleClass().add(styleClass);
                                customStyle = styleClass;
                            }
                        }
                    }
                }
            }
        }
    }
}
