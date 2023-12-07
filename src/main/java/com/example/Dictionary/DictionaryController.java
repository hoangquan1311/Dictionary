package com.example.Dictionary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.googleAPI.TranslatorAPIwithScripts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.stage.Stage;

public class DictionaryController {

    private DictionaryGUIManagement dictionaryManager = new DictionaryGUIManagement();
    private String selectedWord = "";
    private int selectedWordIndex = 0;

    @FXML
    private ListView<String> searchListView;
    @FXML
    private TextField searchBar;
    @FXML
    private Label target;
    @FXML
    private TextArea definition;
    @FXML
    private TextArea spelling;
    @FXML
    private TextArea GGAPIText;
    @FXML
    private TextArea GGAPITranslate;
    @FXML
    private Button eraseButton;
    @FXML
    private Button speechButton;
    @FXML
    private Button editButton;
    @FXML
    private Button SaveEditButton;

    // Handles the event when the "Edit" button is pressed.
    @FXML
    void editButton() {
        editButton.setDisable(true);
        eraseButton.setDisable(true);

        if (definition.isEditable()) {
            disableEditing();
        } else {
            enableEditing();
        }

        SaveEditButton.setVisible(true);
    }

    // Enables editing of the word's information.
    private void enableEditing() {
        definition.setEditable(true);
        definition.setBlendMode(BlendMode.SRC_OVER);
        spelling.setEditable(true);
        spelling.setBlendMode(BlendMode.SRC_OVER);
    }

    // Disables editing of the word's information.
    private void disableEditing() {
        definition.setEditable(false);
        definition.setBlendMode(BlendMode.DARKEN);
        spelling.setEditable(false);
        spelling.setBlendMode(BlendMode.DARKEN);
    }

    // Saves the edited word information.
    @FXML
    void SaveEditWord(ActionEvent event) {
        editButton.setDisable(false);
        SaveEditButton.setVisible(false);
        eraseButton.setDisable(false);

        disableEditing();

        Word word = new Word(selectedWord, spelling.getText(), definition.getText());
        if (dictionaryManager.editWord(word)) {
            showEditSuccessAlert();
        }
    }

    // Displays a success alert after editing.
    private void showEditSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Edit success : " + selectedWord);
        alert.setContentText("Please press UPDATE to save your change");
        alert.showAndWait();
    }

    // Handles the event when the "Translate" button is pressed.
    @FXML
    void TranslateAPIButton(ActionEvent event) throws IOException, InterruptedException {
        GGAPITranslate.setText(TranslatorAPIwithScripts.translate("", "vi", GGAPIText.getText()));
    }

    // Handles the event when the "Speak" button is pressed.
    @FXML
    void sound(ActionEvent event) {
        Speak.speak(selectedWord);
    }

    // Handles the event when the "Export to File" button is pressed.
    @FXML
    void xuatfile() throws IOException {
        dictionaryManager.exportDictGUIDataToFile();
        showUpdateSuccessAlert();
    }

    // Displays a success alert after updating the file.
    private void showUpdateSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update");
        alert.setHeaderText("Update file  ");
        alert.showAndWait();
    }

    // Disables buttons and text areas.
    private void disableButtonAndTextArea() {
        eraseButton.setDisable(true);
        speechButton.setDisable(true);
        editButton.setDisable(true);
        disableEditing();
    }

    // Resets the state of buttons and text areas.
    private void resetButtonAndTextAreaState() {
        eraseButton.setDisable(false);
        speechButton.setDisable(false);
        editButton.setDisable(false);

        SaveEditButton.setVisible(false);

        disableEditing();
    }

    // Handles the event when the "Minus" button is pressed.
    @FXML
    void minus(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Erase Word");
        alert.setHeaderText("Do you want to erase this word ?");
        alert.setContentText(target.getText());
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton) {
            handleEraseWord(event);
        } else if (result.get() == noButton) {
            event.consume();
        }
    }

    // Handles the event to erase a word from the dictionary.
    private void handleEraseWord(ActionEvent event) {
        int previousSearchListViewIndex = searchListView.getSelectionModel().getSelectedIndex();
        dictionaryManager.removeWord(selectedWordIndex);
        updateListView();
        searchListView.getSelectionModel().select(previousSearchListViewIndex);
        if (searchListView.getItems().isEmpty()) {
            clearTextFieldsAndDisableButtons();
        }
    }

    // Clears text fields and disables buttons.
    private void clearTextFieldsAndDisableButtons() {
        target.setText("");
        definition.setText("");
        spelling.setText("");
        disableButtonAndTextArea();
    }

    // Handles the event when the "Plus" button is pressed.
    @FXML
    void plus(ActionEvent event) throws IOException {
        openPlusScene();
        updateListView();
    }

    // Opens the scene for adding a new word.
    private void openPlusScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("plusScene.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root));
        primaryStage.showAndWait();
    }

    // Searches for words in the dictionary.
    private ArrayList<String> searchList(String searchWords, ArrayList<Word> dict) {
        List<String> searchResultArray = dict.stream()
                .filter(word -> word.getWord_target().startsWith(searchWords))
                .map(Word::getWord_target)
                .collect(Collectors.toList());
        return (ArrayList<String>) searchResultArray;
    }

    // Creates the list view with the search result.
    private void createListView(ArrayList<String> dict) {
        searchListView.getItems().addAll(dict);
    }

    // Updates the list view when there are changes.
    public void updateListView() {
        searchListView.getItems().clear();
        createListView(searchList(searchBar.getText(), DictionaryGUI.dict));
    }

    // Initializes the controller.
    @FXML
    void initialize() throws IOException {
        initializeComponents();
        loadDictionaryDataFromFile();
        setupSearchBarListener();
        setupListViewSelectionListener();
        selectFirstItemInListView();
    }

    // Initializes components.
    private void initializeComponents() {
        assert searchBar != null : "fx:id=\"searchBar\" check  FXML file 'gammatest.fxml'.";
        assert target != null : "fx:id=\"copy\" check FXML file 'gammatest.fxml'.";
        assert definition != null : "fx:id=\"defiinition\" check FXML file 'gammatest.fxml'.";
    }

    // Loads dictionary data from a file on application startup.
    private void loadDictionaryDataFromFile() {
        try {
            dictionaryManager.insertDictGUIDataFromFile();
            updateListViewWithData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Updates the list view with data.
    private void updateListViewWithData() {
        createListView(searchList(searchBar.getText(), DictionaryGUI.dict));
    }

    // Sets up a listener for the search bar.
    private void setupSearchBarListener() {
        searchBar.textProperty().addListener((observableValue, s, t1) -> updateListView());
    }

    // Sets up a listener for the list view selection.
    private void setupListViewSelectionListener() {
        searchListView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> handleListViewSelection());
    }

    // Handles the selection event in the list view.
    private void handleListViewSelection() {
        if (isSelectedWordNotEmpty()) {
            setSelectedWordAndIndex();
            updateTextFieldsAndButtons();
            handleSelectedWordIndex();
        }
    }

    // Checks if the selected word is not empty.
    private boolean isSelectedWordNotEmpty() {
        return searchListView.getSelectionModel().getSelectedItem() != null;
    }

    // Sets the selected word and index.
    private void setSelectedWordAndIndex() {
        selectedWord = searchListView.getSelectionModel().getSelectedItem();
        selectedWordIndex = dictionaryManager.DictionaryGUILookup(selectedWord);
    }

    // Updates text fields and buttons based on the selected word.
    private void updateTextFieldsAndButtons() {
        target.setText(selectedWord);
        spelling.setText(DictionaryGUI.dict.get(selectedWordIndex).getWord_spelling());
        definition.setText(DictionaryGUI.dict.get(selectedWordIndex).getWord_explain());
        resetButtonAndTextAreaState();
    }

    // Handles the selected word index.
    private void handleSelectedWordIndex() {
        if (selectedWordIndex == 0) {
            disableButtonAndTextArea();
        }
    }

    // Selects the first item in the list view.
    private void selectFirstItemInListView() {
        searchListView.getSelectionModel().select(0);
    }
}
