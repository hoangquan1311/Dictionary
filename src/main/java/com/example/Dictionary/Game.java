package com.example.Dictionary;
import com.example.Dictionary.*;

import java.util.*;

public class Game extends Word {
    Scanner scanner = new Scanner(System.in);
    Random random = new Random();
    String[] options = new String[4];
    String questionWord;
    String meaning;
    int markFuction = 0;
    String noti;
    String getQuestionForGraphic;
     void getQuestion() {
        getQuestionForGraphic = "";
        int sizeWordArray = DictionaryGUI.dict.size();
        if (sizeWordArray > 0) {
            int randomIndex = random.nextInt(sizeWordArray);
            questionWord = DictionaryGUI.dict.get(randomIndex).getWord_target();
            meaning = DictionaryGUI.dict.get(randomIndex).getWord_explain();
            getQuestionForGraphic = getQuestionForGraphic + "Question: What is the meaning of \"" + meaning + "\" ? \n";
            if (questionWord != null) {
                options[0] = questionWord;
            }
            for (int i = 1; i < 4; i++) {
                int randomOptionIndex = random.nextInt(DictionaryGUI.dict.size());
                String option = DictionaryGUI.dict.get(randomOptionIndex).getWord_target();
                options[i] = option;
            }
            shuffleArray(options);
            for (int i = 0; i < 4; i++) {
                String option = options[i];
                getQuestionForGraphic = getQuestionForGraphic + (i + 1) + ". " + option + "\n";
            }
            getQuestionForGraphic += "Your choice (1-4): \n";
        }
        markFuction = 1;
    }

    void answerQ(String answer) {
        noti = "";
        if (answer == null || answer.isEmpty()) {
            while (true) {
                answer = scanner.nextLine();
                if (Integer.parseInt(answer) >= 1 && Integer.parseInt(answer) <= 4) {
                    break;
                }
            }
        }
        if (options[Integer.parseInt(answer) - 1].equals(questionWord)) {
            noti = noti + "Correct \n";
        } else {
            noti = noti + "Incorrect. The correct answer is: " + questionWord + "\n";
        }
        markFuction = 2;
    }

    //Trộn mảng
    static void shuffleArray(String[] array) {
        int n = array.length;
        Random random = new Random();

        for (int i = n - 1; i > 0; i--) {
            int randomIndex = random.nextInt(i + 1);
            String temp = array[i];
            array[i] = array[randomIndex];
            array[randomIndex] = temp;
        }
    }

}
