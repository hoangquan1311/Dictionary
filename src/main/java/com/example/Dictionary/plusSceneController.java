package com.example.Dictionary;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class plusSceneController {
    private DictionaryGUIManagement dictionaryManager = new DictionaryGUIManagement();

    // FXML annotations to inject components from the FXML file
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

    // Event handler for the Cancel button
    @FXML
    void Cancel(ActionEvent e) {
        // Close the current stage (window)
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }

    // Event handler for the ADD button
    @FXML
    void add(ActionEvent e) throws IOException {
        // Retrieve input values from the text fields
        String target = addNewWord.getText();
        String spelling = addNewSpelling.getText();
        String definition = addNewDefinition.getText();

        // Attempt to add the word to the dictionary using a management class
        if (dictionaryManager.addWord(target, spelling, definition)) {
            // If successful, close the current stage
            closeStage();
        } else {
            // If unsuccessful, show an error alert
            showErrorAlert("Duplicates", "Error");
        }
    }

    // Close the current stage
    private void closeStage() {
        Stage stage = (Stage) Cancel.getScene().getWindow();
        stage.close();
    }

    // Show an error alert with the specified header text and title
    private void showErrorAlert(String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    // Initialization method called after FXML components are injected
    @FXML
    void initialize() {
        // Check if FXML components are properly injected
        if (addNewWord == null) {
            throw new IllegalStateException("fx:id=\"addNewWord\" chưa được đưa vào: hãy kiểm tra tệp FXML của bạn 'plusScene.fxml'.");
        }
        if (addNewSpelling == null) {
            throw new IllegalStateException("fx:id=\"addNewSpelling\" chưa được đưa vào: hãy kiểm tra tệp FXML của bạn 'plusScene.fxml'.");
        }
        if (addNewDefinition == null) {
            throw new IllegalStateException("fx:id=\"addNewDefinition\" chưa được đưa vào: hãy kiểm tra tệp FXML của bạn 'plusScene.fxml'.");
        }
        if (ADD == null) {
            throw new IllegalStateException("fx:id=\"ADD\" chưa được đưa vào: hãy kiểm tra tệp FXML của bạn 'plusScene.fxml'.");
        }
        if (Cancel == null) {
            throw new IllegalStateException("fx:id=\"Cancel\" chưa được đưa vào: hãy kiểm tra tệp FXML của bạn 'plusScene.fxml'.");
        }
    }

}
