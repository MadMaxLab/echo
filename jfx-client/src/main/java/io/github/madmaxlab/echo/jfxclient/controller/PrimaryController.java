package io.github.madmaxlab.echo.jfxclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;



public class PrimaryController {
    @FXML
    ListView<String> contactList;
    @FXML
    private TextField messageField;
    @FXML
    private TextArea messagesArea;



    @FXML
    public void onClick() {
        System.out.println("Click!");
        contactList.getItems().add("Click!");
    }

    @FXML
    public void onMessageFieldKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)){
            messagesArea.appendText("Me: " + messageField.getText() + System.lineSeparator());
            messageField.clear();
        }

    }
}
