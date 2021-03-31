package com.treemoval.gui;

import com.treemoval.data.Forest;
import com.treemoval.visualizer.ForestGroup;
import com.treemoval.visualizer.ForestScene;
import com.treemoval.data.Forest;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class TreemovalController {

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField filepathTextField;
    @FXML
    private Button exitButton;
    @FXML
    private ForestScene forestSubScene;
    @FXML
    private Button forestGenerationButton;

    @FXML
    public void initialize() {

        Forest forest = new Forest(2000, 3000);
        ForestGroup forestGroup = new ForestGroup(forest);

        forestSubScene.init(forestGroup);

        forestSubScene.heightProperty().bind(borderPane.heightProperty());
        forestSubScene.widthProperty().bind(borderPane.widthProperty());
    }

    //--------------------------------------------------------------------------------------------------
    // TreemovalController::filePathOnEnter
    //
    /**
     * Adds the events for handling user inputted file paths to
     * the GUI text field.
     *
     * todo Add error prompt on incorrect file type input (.csv) (found in button method)
     * todo Set the label of the text field to the file path once path is entered
     * todo setFocus
     */
    public void filePathOnEnter(ActionEvent event) throws IOException {
        Stage stage = (Stage) filepathTextField.getScene().getWindow();
        String fileLocation = filepathTextField.getText();

        Forest forest = new Forest();

        String selectedFile = fileLocation;
        String checkCsv = ".csv";


        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "");
        Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION, "");

        errorAlert.initModality(Modality.APPLICATION_MODAL);
        errorAlert.initOwner(stage);
        confirmAlert.initModality(Modality.APPLICATION_MODAL);
        confirmAlert.initOwner(stage);

        errorAlert.getDialogPane().setHeaderText(".csv file not selected. Try again.");

        if(!selectedFile.substring(selectedFile.length() - 4).equals(checkCsv)) {
            filepathTextField.clear();
            errorAlert.showAndWait();
            System.out.println("Not a csv file!");
        } else {
            confirmAlert.getDialogPane().setHeaderText(selectedFile +" was selected");
            filepathTextField.setText(fileLocation);
            confirmAlert.showAndWait();
            filepathTextField.setText(selectedFile);
            forest.readFromFile(fileLocation);
            System.out.println(selectedFile +" was selected");
        }

    }

    //--------------------------------------------------------------------------------------------------
    // TreemovalController::exitButtonOnAction
    //
    /**
     * Adds the ActionEvent for the GUI exit button
     */
    public void exitButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    //--------------------------------------------------------------------------------------------------
    // TreemovalController::fileFinderButtonOnAction
    //
    /**
     * Adds the ActionEvent for the GUI file search button
     *
     * todo Open the selected file to be read as forest data
     */
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
                filepathTextField.setText(selectedFile);
                System.out.println(selectedFile +" was selected");
            }
        }
    }

    //--------------------------------------------------------------------------------------------------
    // TreemovalController::forestGenerationButtonOnAction
    //
    /**
     * Adds the ActionEvent for the GUI forest generator button
     * todo Complete error checking input (require 2 integer inputs etc)
     */
    public void forestGenerationButtonOnAction(ActionEvent event) {

        TextInputDialog genParameterInput = new TextInputDialog();
        genParameterInput.setHeaderText("Enter generation parameters (#trees,bound)");
        genParameterInput.setTitle("Tree Generation Parameters");
        boolean validInput;

        Optional<String> parameters = genParameterInput.showAndWait();

        if(parameters.isPresent()) {
            String input = parameters.get();
            String values[] = input.split(",");
            System.out.println(values[0] + " " + values[1]);

            if(values.length != 2) {
                System.out.println("Invalid input");
                return;
            }

            try {
                Integer.parseInt(values[0]);
                Integer.parseInt(values[0]);
                validInput = true;

            } catch(Exception e) {
                validInput = false;
            }

            if(validInput) {
                System.out.println("Valid Input:" + Integer.parseInt(values[0]) + " " +Integer.parseInt(values[1]));
                //Forest forest = new Forest(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
            } else {
                System.out.println("Invalid Input");
            }

        }
    }
}
