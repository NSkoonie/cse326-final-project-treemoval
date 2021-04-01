package com.treemoval.gui;

import com.treemoval.data.Forest;
import com.treemoval.visualizer.ForestGroup;
import com.treemoval.visualizer.ForestScene;
import com.treemoval.data.Forest;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class TreemovalController {

    // members from fxml document
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField filepathTextField;
    @FXML
    private Button exitButton;
    @FXML
    private ForestScene forestSubScene;
    @FXML
    private Button exportButton;
    @FXML
    private Button runAlgorithmbutton;
    @FXML
    private Button forestGenerationButton;
    @FXML
    private CheckBox showMarkedTreesCheckBox;

    private Forest currentForest;
    private ForestGroup currentForestGroup;

    //--------------------------------------------------------------------------------------------------
    // TreemovalController::getCurrentForest
    //
    /**
     *
     */
    public Forest getCurrentForest() {
        return currentForest;
    }
    //--------------------------------------------------------------------------------------------------
    // TreemovalController::setCurrentForest
    //
    /**
     *
     */
    public void setCurrentforest(Forest forest) {
        this.currentForest = forest;
    }

    //--------------------------------------------------------------------------------------------------
    // TreemovalController::initialize
    //
    /**
     * initializes fxml members
     * (we assume this is called at the end of FXMLLoader.load()?)
     */
    @FXML
    public void initialize() {

        Forest forest = new Forest(0, 0);
        ForestGroup forestGroup = new ForestGroup(forest);

        forestSubScene.init(forestGroup);

        forestSubScene.heightProperty().bind(borderPane.heightProperty());
        forestSubScene.widthProperty().bind(borderPane.widthProperty());
    }

    //--------------------------------------------------------------------------------------------------
    // TreemovalController::loadForest
    //
    public void loadForest(Forest forest) {

        currentForestGroup = new ForestGroup(forest);
        forestSubScene.setForestGroup(currentForestGroup);

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
            forest.readFromFile(selectedFile);

            loadForest(forest);
            setCurrentforest(forest);
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

                Forest forest = new Forest(selectedFile);
                loadForest(forest);
                setCurrentforest(forest);
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

                Forest forest = new Forest(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                loadForest(forest);

            } else {
                System.out.println("Invalid Input");
            }

        }
    }

    //--------------------------------------------------------------------------------------------------
    // TreemovalController::runAlgorithmButtonOnAction
    //
    public void runAlgorithmButtonOnAction(ActionEvent event) {

        Forest newForest = currentForest.runThinningAlgorithmNewForest(25);
        loadForest(currentForest);

    }

    //--------------------------------------------------------------------------------------------------
    // TreemovalController::showMarkedTreesCheckBoxOnAction
    //
    /**
     * Adds the ActionEvent for the GUI exit button
     */
    public void showMarkedTreesCheckBoxOnAction(ActionEvent event) {
        if(showMarkedTreesCheckBox.isSelected()) {
            currentForestGroup.showRedTrees();
        } else {
            currentForestGroup.hideRedTrees();
        }
    }

}
