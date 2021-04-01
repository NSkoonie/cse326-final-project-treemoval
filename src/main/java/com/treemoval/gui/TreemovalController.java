package com.treemoval.gui;

import com.treemoval.data.Forest;
import com.treemoval.visualizer.ForestGroup;
import com.treemoval.visualizer.ForestScene;
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
    private TextField filePathTextField;
    @FXML
    private TextField exportPathTextField;
    @FXML
    private Button exitButton;
    @FXML
    private Button fileFinderButton;
    @FXML
    private ForestScene forestSubScene;
    @FXML
    private Button exportButton;
    @FXML
    private Button importButton;
    @FXML
    private Button runAlgorithmbutton;
    @FXML
    private Button forestGenerationButton;
    @FXML
    private CheckBox showMarkedTreesCheckBox;

    private Forest currentForest;
    private ForestGroup currentForestGroup;
    private String filePath;
    private String exportPath;

    public Forest getCurrentForest() { return currentForest;}
    public void setCurrentforest(Forest forest) { this.currentForest = forest;}

    public String getFilepath() { return filePath;}
    public void setFilePath(String filePath) { this.filePath = filePath;}

    public String getExportpath() { return exportPath;}
    public void setExportPath(String exportPath) { this.exportPath = exportPath;}

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

        setCurrentforest(forest);
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
        Stage stage = (Stage) filePathTextField.getScene().getWindow();
        String fileLocation = filePathTextField.getText();

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
            filePathTextField.clear();
            errorAlert.showAndWait();
            System.out.println("Not a csv file!");
        } else {
            confirmAlert.getDialogPane().setHeaderText(selectedFile +" was selected");
            filePathTextField.setText(fileLocation);
            confirmAlert.showAndWait();
            filePathTextField.setText(selectedFile);
            forest.readFromFile(fileLocation);
            System.out.println(selectedFile +" was selected");
            forest.readFromFile(selectedFile);

            loadForest(forest);
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
        Stage stage = (Stage) fileFinderButton.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(stage);

        if(file != null) {
            String selectedFile = file.getAbsolutePath();

            filePathTextField.setText(selectedFile);
            System.out.println(selectedFile +" was selected");

            setFilePath(selectedFile);

        }
    }

    //--------------------------------------------------------------------------------------------------
    // TreemovalController::exportFinderButtonOnAction
    //
    /**
     * Adds the ActionEvent for the GUI file export selection button
     */
    public void exportFinderButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) exportButton.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showSaveDialog(stage);

        if(file != null) {
            String exportLocation = file.getAbsolutePath();
            String fileName = file.getName();
            System.out.println(exportLocation);

            exportPathTextField.setText(exportLocation);
            System.out.println(fileName +" will be saved to " + exportLocation);

            setExportPath(exportLocation);
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
    /**
     *
     */
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

    //--------------------------------------------------------------------------------------------------
    // TreemovalController::importButtonOnAction
    //
    /**
     *
     */
    public void importButtonOnAction(ActionEvent event) {
        Forest forest = new Forest(getFilepath());
        loadForest(forest);
    }

    //--------------------------------------------------------------------------------------------------
    // TreemovalController::exportButtonOnAction
    //
    /**
     *
     */
    public void exportButtonOnAction(ActionEvent event) throws IOException {
      currentForest.exportForest(exportPath);
    }

}
