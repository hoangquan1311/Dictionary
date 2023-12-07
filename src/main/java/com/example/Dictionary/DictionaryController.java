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

    @FXML private ListView<String> searchListView;
    @FXML private TextField searchBar;
    @FXML private Label target;
    @FXML private TextArea definition;
    @FXML private TextArea spelling;
    @FXML private TextArea GGAPIText;
    @FXML private TextArea GGAPITranslate;
    @FXML private Button eraseButton;
    @FXML private Button speechButton;
    @FXML private Button editButton;
    @FXML private Button SaveEditButton;

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

    private void enableEditing() {
        definition.setEditable(true);
        definition.setBlendMode(BlendMode.SRC_OVER);
        spelling.setEditable(true);
        spelling.setBlendMode(BlendMode.SRC_OVER);
    }

    private void disableEditing() {
        definition.setEditable(false);
        definition.setBlendMode(BlendMode.DARKEN);
        spelling.setEditable(false);
        spelling.setBlendMode(BlendMode.DARKEN);
    }

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

    private void showEditSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucsess");
        alert.setHeaderText("Edit success : " + selectedWord);
        alert.setContentText("Please press UPDATE to save your change");
        alert.showAndWait();
    }

    @FXML
    void TranslateAPIButton(ActionEvent event) throws IOException, InterruptedException {
        GGAPITranslate.setText(TranslatorAPIwithScripts.translate("", "vi", GGAPIText.getText()));
    }

    @FXML
    void sound(ActionEvent event) {
        Speak.speak(selectedWord);
    }

    @FXML
    void xuatfile() throws IOException {
        dictionaryManager.exportDictGUIDataToFile();
        showUpdateSuccessAlert();
    }

    private void showUpdateSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update");
        alert.setHeaderText("Update file  ");
        alert.showAndWait();
    }

    private void disableButtonAndTextArea() {
        eraseButton.setDisable(true);
        speechButton.setDisable(true);
        editButton.setDisable(true);
        disableEditing();
    }

    private void resetButtonAndTextAreaState() {
        eraseButton.setDisable(false);
        speechButton.setDisable(false);
        editButton.setDisable(false);

        SaveEditButton.setVisible(false);

        disableEditing();
    }

    @FXML
    void minus(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("erase word");
        alert.setHeaderText("Are you sure delete word ?");
        alert.setContentText(target.getText());
        ButtonType yesButton = new ButtonType("Yes!!");
        ButtonType noButton = new ButtonType("No!!");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton) {
            handleEraseWord(event);
        } else if (result.get() == noButton) {
            event.consume();
        }
    }

    private void handleEraseWord(ActionEvent event) {
        int previousSearchListViewIndex = searchListView.getSelectionModel().getSelectedIndex();
        dictionaryManager.removeWord(selectedWordIndex);
        updateListView();
        searchListView.getSelectionModel().select(previousSearchListViewIndex);
        if (searchListView.getItems().isEmpty()) {
            clearTextFieldsAndDisableButtons();
        }
    }

    private void clearTextFieldsAndDisableButtons() {
        target.setText("");
        definition.setText("");
        spelling.setText("");
        disableButtonAndTextArea();
    }

    @FXML
    void plus(ActionEvent event) throws IOException {
        openPlusScene();
        updateListView();
    }

    private void openPlusScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("plusScene.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root));
        primaryStage.showAndWait();
    }

    private ArrayList<String> searchList(String searchWords, ArrayList<Word> dict) {
        List<String> searchResultArray = dict.stream()
                .filter(word -> word.getWord_target().startsWith(searchWords))
                .map(Word::getWord_target)
                .collect(Collectors.toList());
        return (ArrayList<String>) searchResultArray;
    }

    private void createListView(ArrayList<String> dict) {
        searchListView.getItems().addAll(dict);
    }

    public void updateListView() {
        searchListView.getItems().clear();
        createListView(searchList(searchBar.getText(), DictionaryGUI.dict));
    }

    @FXML
    void initialize() throws IOException {
        initializeComponents();
        loadDictionaryDataFromFile();
        setupSearchBarListener();
        setupListViewSelectionListener();
        selectFirstItemInListView();
    }

    private void initializeComponents() {
        assert searchBar != null : "fx:id=\"searchBar\" check  FXML file 'gammatest.fxml'.";
        assert target != null : "fx:id=\"copy\" check FXML file 'gammatest.fxml'.";
        assert definition != null : "fx:id=\"defiinition\" check FXML file 'gammatest.fxml'.";
    }

    private void loadDictionaryDataFromFile() {
        try {
            dictionaryManager.insertDictGUIDataFromFile();
            updateListViewWithData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateListViewWithData() {
        createListView(searchList(searchBar.getText(), DictionaryGUI.dict));
    }

    private void setupSearchBarListener() {
        searchBar.textProperty().addListener((observableValue, s, t1) -> updateListView());
    }

    private void setupListViewSelectionListener() {
        searchListView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> handleListViewSelection());
    }

    private void handleListViewSelection() {
        if (isSelectedWordNotEmpty()) {
            setSelectedWordAndIndex();
            updateTextFieldsAndButtons();
            handleSelectedWordIndex();
        }
    }

    private boolean isSelectedWordNotEmpty() {
        return searchListView.getSelectionModel().getSelectedItem() != null;
    }

    private void setSelectedWordAndIndex() {
        selectedWord = searchListView.getSelectionModel().getSelectedItem();
        selectedWordIndex = dictionaryManager.DictionaryGUILookup(selectedWord);
    }

    private void updateTextFieldsAndButtons() {
        target.setText(selectedWord);
        spelling.setText(DictionaryGUI.dict.get(selectedWordIndex).getWord_spelling());
        definition.setText(DictionaryGUI.dict.get(selectedWordIndex).getWord_explain());
        resetButtonAndTextAreaState();
    }

    private void handleSelectedWordIndex() {
        if (selectedWordIndex == 0) {
            disableButtonAndTextArea();
        }
    }

    private void selectFirstItemInListView() {
        searchListView.getSelectionModel().select(0);
    }
}
