package com.treemoval.gui;

import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import com.treemoval.data.Forest;

import java.io.File;

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
    @FXML
    private Button fileFinderButton;

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

    public void fileFinderButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);

        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "");
        Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION, "");

        errorAlert.initModality(Modality.APPLICATION_MODAL);
        errorAlert.initOwner(stage);
        confirmAlert.initModality(Modality.APPLICATION_MODAL);
        confirmAlert.initOwner(stage);

        errorAlert.getDialogPane().setHeaderText(".csv file not selected. Try again.");

        if(file != null) {
            String selectedFile = file.getAbsolutePath();
            String checkCsv = ".csv";

            if(!selectedFile.substring(selectedFile.length() - 4).equals(checkCsv)) {
                errorAlert.showAndWait();
                System.out.println("Not a csv file!");
            } else {
                confirmAlert.getDialogPane().setHeaderText(selectedFile +" was selected");
                confirmAlert.showAndWait();
                System.out.println(selectedFile +" was selected");
            }
        }
    }
}
