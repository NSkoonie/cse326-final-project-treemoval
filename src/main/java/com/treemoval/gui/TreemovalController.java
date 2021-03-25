package com.treemoval.gui;

import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class TreemovalController {

    @FXML
    private TextField filepathTextField;
    @FXML
    private Button exitButton;
    @FXML
    private Button forestGenerationButton;
    @FXML
    private Button runAlgorithmButton;
    @FXML
    private Button visualizerStartButton;
    @FXML
    private Button exportButton;
    @FXML
    private SubScene forestSubScene;

    public void filePathOnEnter(ActionEvent event) { // todo setFocus
        String fileLocation = filepathTextField.getText();
        /*
        readfromFile(fileLocation);

        set function to boolean to relay GUI message?
        if (readfromFile(fileLocation) = true){
            display message "Data sucessfully read"
        } else {
            Display message "Please enter valid file path"
        }
         */
        System.out.println(fileLocation);
    }

    public void exitButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
