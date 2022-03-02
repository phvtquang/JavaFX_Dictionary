package com.example.dictionaryApplication;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class plusSceneController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField addNewWord;

    @FXML
    private TextField addNewSpelling;

    @FXML
    private TextArea addNewDefinition;

    @FXML
    private Button ADD;

    @FXML
    private Button Cancel;

    @FXML
    void Cancel(ActionEvent event) {
        Stage stage = (Stage) Cancel.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    void add(ActionEvent event) throws IOException { // nút add trong cửa sổ thêm từ

        String target = addNewWord.getText();
        String spelling = addNewSpelling.getText();
        String def = addNewDefinition.getText();
        if (DictionaryGUIManagement.addWord(target,spelling,def)) {
            //System.out.println("ADD THANH CONG");
            Stage stage = (Stage) Cancel.getScene().getWindow();
            stage.close();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Duplicate ?");
            alert.showAndWait();
            //System.out.println("THAT BAI");
        }
    }



    void initialize() {
        assert addNewWord != null : "fx:id=\"addNewWord\" was not injected: check your FXML file 'plusScene.fxml'.";
        assert addNewSpelling != null : "fx:id=\"addNewSpelling\" was not injected: check your FXML file 'plusScene.fxml'.";
        assert addNewDefinition != null : "fx:id=\"addNewDefinition\" was not injected: check your FXML file 'plusScene.fxml'.";
        assert ADD != null : "fx:id=\"ADD\" was not injected: check your FXML file 'plusScene.fxml'.";
        assert Cancel != null : "fx:id=\"Cancel\" was not injected: check your FXML file 'plusScene.fxml'.";

    }
}