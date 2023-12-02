package com.example.Dictionary;

import java.io.*;
import java.util.Objects;

public class DictionaryGUIManagement {

    // Method to insert data from a file into the dictionary
    public static void insertDictGUIDataFromFile() throws IOException {
        try {
            // Open the file for reading
            File inputFile = new File("src/main/resources/E_V.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));

            int targetEndIndex = 0;
            while (bufferedReader.ready()) {
                // Read each line from the file
                String line = bufferedReader.readLine();
                if (!line.isEmpty()) {
                    // Check if the line starts with '@' (indicating a new word)
                    if (line.charAt(0) == '@') {
                        // Create a new Word object with empty values
                        Word newWord = new Word("", "", "");
                        DictionaryGUI.dict.add(newWord);
                        targetEndIndex = line.length();

                        // Extract target word and spelling
                        for (int i = 1; i < line.length(); i++) {
                            if (line.charAt(i) != '/') {
                                DictionaryGUI.dict.get(DictionaryGUI.dict.size() - 1).setWord_target(
                                        DictionaryGUI.dict.get(DictionaryGUI.dict.size() - 1).getWord_target() + line.charAt(i)
                                );
                            } else {
                                targetEndIndex = i;
                                break;
                            }
                        }
                        // Set the spelling of the word
                        DictionaryGUI.dict.get(DictionaryGUI.dict.size() - 1).setWord_spelling(line.substring(targetEndIndex));
                    } else {
                        // Append the explanation to the existing word
                        DictionaryGUI.dict.get(DictionaryGUI.dict.size() - 1).setWord_explain(
                                DictionaryGUI.dict.get(DictionaryGUI.dict.size() - 1).getWord_explain() + line + System.getProperty("line.separator")
                        );
                    }
                }
            }
            // Close the BufferedReader
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Please check file directory");
        }
    }

    // Method to export dictionary data to a file
    public static void exportDictGUIDataToFile() throws IOException {
        // Open the file for writing
        File outputFile = new File("src/main/resources/E_V.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));

        // Iterate through the dictionary and write data to the file
        for (int i = 1; i < DictionaryGUI.dict.size(); i++) {
            bufferedWriter.write("@" + DictionaryGUI.dict.get(i).getWord_target());
            bufferedWriter.write(" " + DictionaryGUI.dict.get(i).getWord_spelling());
            bufferedWriter.newLine();
            bufferedWriter.write(DictionaryGUI.dict.get(i).getWord_explain());
            bufferedWriter.newLine();
        }
        // Close the BufferedWriter
        bufferedWriter.close();
    }

    // Method to compare two strings
    private static int compareWords(String word1, String word2) {
        int index = 0;
        while (index < word1.length() && index < word2.length()) {
            if (word1.charAt(index) > word2.charAt(index)) {
                return 1;
            }
            if (word1.charAt(index) < word2.charAt(index)) {
                return -1;
            }
            index++;
        }
        return Integer.compare(word1.length(), word2.length());
    }

    // Method to add a new word to the dictionary
    public static boolean addWord(String target, String spelling, String definition) {
        Word newWord = new Word(target, spelling, definition);
        for (int i = 1; i < DictionaryGUI.dict.size() - 1; i++) {
            if (compareWords(DictionaryGUI.dict.get(i).getWord_target(), target) == 0) {
                return false;
            }

            if (compareWords(DictionaryGUI.dict.get(i).getWord_target(), target) < 0 &&
                    compareWords(DictionaryGUI.dict.get(i + 1).getWord_target(), target) > 0) {
                DictionaryGUI.dict.add(i + 1, newWord);
                return true;
            }
        }
        DictionaryGUI.dict.add(newWord);
        return true;
    }

    // Method to remove a word from the dictionary
    public static void removeWord(int index) {
        DictionaryGUI.dict.remove(index);
    }

    // Method to edit an existing word in the dictionary
    public static boolean editWord(Word editedWord) {
        for (int i = 0; i < DictionaryGUI.dict.size(); i++) {
            if (Objects.equals(DictionaryGUI.dict.get(i).getWord_target(), editedWord.getWord_target())) {
                DictionaryGUI.dict.set(i, editedWord);
                return true;
            }
        }
        return false;
    }

    // Method to lookup a word in the dictionary and return its index
    public static int DictionaryGUILookup(String targetWord) {
        for (int i = 1; i < DictionaryGUI.dict.size(); i++) {
            if (DictionaryGUI.dict.get(i).getWord_target().equals(targetWord)) {
                return i;
            }
        }
        return 0;
    }
}
