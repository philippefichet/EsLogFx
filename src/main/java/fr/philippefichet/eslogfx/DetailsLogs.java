/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.philippefichet.eslogfx;

import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author philippefichet
 */
public class DetailsLogs extends BorderPane {

    private String content;
    
    public DetailsLogs(String content) {
        this.content = content;
        TextArea area = new TextArea(content);
        area.setEditable(false);
        setCenter(area);
    }
}
