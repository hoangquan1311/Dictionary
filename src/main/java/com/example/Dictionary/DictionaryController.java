package com.example.Dictionary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import com.example.googleAPI.TranslatorAPIwithScripts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class DictionaryController  {


    String selectedWord = "";
    int selectedWordIndex = 0;


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

    public Button gameButton;

    @FXML
    void editButton() {
        editButton.setDisable(true);
        eraseButton.setDisable(true);
        if (definition.isEditable()) {
            definition.setEditable(false);
            definition.setBlendMode(BlendMode.DARKEN);
            spelling.setEditable(false);
            spelling.setBlendMode(BlendMode.DARKEN);
        } else {
            definition.setEditable(true);
            definition.setBlendMode(BlendMode.SRC_OVER);
            spelling.setEditable(true);
            spelling.setBlendMode(BlendMode.SRC_OVER);
        }
        SaveEditButton.setVisible(true);
    }

    @FXML
    void SaveEditWord(ActionEvent event) {
        editButton.setDisable(false);
        SaveEditButton.setVisible(false);
        eraseButton.setDisable(false);
        gameButton.setDisable(false);

        definition.setEditable(false);
        definition.setBlendMode(BlendMode.DARKEN);
        spelling.setEditable(false);
        spelling.setBlendMode(BlendMode.DARKEN);

        Word word = new Word(selectedWord, spelling.getText(), definition.getText());
        if (DictionaryGUIManagement.editWord(word)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SUCCESS");
            alert.setHeaderText("Edit success : " + selectedWord);
            alert.setContentText("Please press UPDATE to save your change");
            alert.showAndWait();
        }

    }

    @FXML
    void TranslateAPIButton(ActionEvent event) throws IOException, InterruptedException {
        GGAPITranslate.setText(TranslatorAPIwithScripts.translate("", "vi", GGAPIText.getText()));
    }

    @FXML
    void sound(ActionEvent event) { // đọc từ
        Speak.speak(selectedWord);
    }

    @FXML
    void xuatfile() throws IOException {
        DictionaryGUIManagement.exportDictGUIDataToFile();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update Success");
        alert.setHeaderText("Update Success To File ");
        alert.showAndWait();
    }

    private void disableButtonAndTextArea() {
        eraseButton.setDisable(true);
        speechButton.setDisable(true);
        editButton.setDisable(true);
        gameButton.setDisable(true);

        definition.setEditable(false);
        definition.setBlendMode(BlendMode.DARKEN);
        spelling.setEditable(false);
        spelling.setBlendMode(BlendMode.DARKEN);
    }

    private void resetButtonAndTextAreaState() {
        eraseButton.setDisable(false);
        speechButton.setDisable(false);
        editButton.setDisable(false);

        SaveEditButton.setVisible(false);

        definition.setEditable(false);
        definition.setBlendMode(BlendMode.DARKEN);
        spelling.setEditable(false);
        spelling.setBlendMode(BlendMode.DARKEN);
    }

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
            int previousSearchListViewIndex = searchListView.getSelectionModel().getSelectedIndex();
            DictionaryGUIManagement.removeWord(selectedWordIndex);
            updateListView();
            searchListView.getSelectionModel().select(previousSearchListViewIndex);
            if (searchListView.getItems().size() == 0) {
                target.setText("");
                definition.setText("");
                spelling.setText("");
                disableButtonAndTextArea();
            }
        } else if (result.get() == noButton) {
            event.consume();
        }
    }

    @FXML
    void plus(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("plusScene.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root));
        primaryStage.showAndWait();
        updateListView();
    }

    private ArrayList<String> searchList(String searchWords, ArrayList<Word> dict) {
        ArrayList<String> searchResultArray = new ArrayList<>();
        for (Word word : dict) {
            if (word.getWord_target().indexOf(searchWords) == 0) {
                searchResultArray.add(word.getWord_target());
            }
        }
        return searchResultArray;
    }

    private void createListView(ArrayList<String> dict) {
        for (String s : dict) {
            searchListView.getItems().add(s);
        }
    }

    public void updateListView() {
        searchListView.getItems().clear();
        createListView(searchList(searchBar.getText(), DictionaryGUI.dict));
    }


    @FXML
    void initialize() throws IOException {
        assert searchBar != null : "fx:id=\"searchBar\" was not injected: check your FXML file 'gammatest.fxml'.";
        assert target != null : "fx:id=\"copy\" was not injected: check your FXML file 'gammatest.fxml'.";
        assert definition != null : "fx:id=\"defiinition\" was not injected: check your FXML file 'gammatest.fxml'.";

        DictionaryGUIManagement.insertDictGUIDataFromFile();

        for (int i = 0; i < DictionaryGUI.dict.size(); i++) {
            searchListView.getItems().add(DictionaryGUI.dict.get(i).getWord_target());
        }

        searchBar.textProperty().addListener(((observableValue, s, t1) -> {
            updateListView();
        }));

        searchListView.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, s, t1) -> {


                    if (t1 != null) {
                        selectedWord = searchListView.getSelectionModel().getSelectedItem();

                        target.setText(selectedWord);

                        selectedWordIndex = DictionaryGUIManagement.DictionaryGUILookup(selectedWord);


                        target.setText(DictionaryGUI.dict.get(selectedWordIndex).getWord_target());
                        spelling.setText(DictionaryGUI.dict.get(selectedWordIndex).getWord_spelling());
                        definition.setText(DictionaryGUI.dict.get(selectedWordIndex).getWord_explain());
                        resetButtonAndTextAreaState();

                        if (selectedWordIndex == 0) {
                            disableButtonAndTextArea();
                        }
                    }
                });

        searchListView.getSelectionModel().select(0);
    }



}




