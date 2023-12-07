package com.example.Dictionary;
import com.example.Dictionary.*;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import static com.example.Dictionary.Word.getNoti;
import static com.example.Dictionary.Word.wordArray;


public class gameController {

    @FXML
    private Button closeButton;

    @FXML
    private TextArea questionTextArea = new TextArea();

    @FXML
    private TextField answerBox;

    @FXML
    private Button okAnswer;

    public Game game1 = new Game();

    @FXML
    public void playGame() {
        answerBox.setVisible(true);
        okAnswer.setVisible(true);
        questionTextArea.setEditable(false);
        game1.getQuestion();
        questionTextArea.setText(game1.getQuestionForGraphic);
    }

    @FXML
    private void handlePlayGameButton(ActionEvent event) {
        playGame();
    }



    @FXML
    public void okBtnListener(ActionEvent event) {
        String answerQuestion = answerBox.getText().replaceAll("\\s+", " ").trim();
        if (game1.markFuction == 1) {
            if (Integer.parseInt(answerQuestion) >= 1 && Integer.parseInt(answerQuestion) <= 4) {
                game1.answerQ(answerQuestion);
                questionTextArea.setText(game1.getQuestionForGraphic + game1.noti);
                answerBox.clear();
            }
        }
    }
    @FXML


    public void setFalseGame() {
        answerBox.setVisible(false);
        okAnswer.setVisible(false);
        answerBox.setText("");
    }
}
