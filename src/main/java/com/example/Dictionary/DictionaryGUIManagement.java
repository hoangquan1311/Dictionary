package com.example.Dictionary;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class DictionaryGUIManagement {

    // Method to insert data from a file into the dictionary
    public static void insertDictGUIDataFromFile() throws IOException {
        // Specify the input file path
        Path inputPath = Paths.get("src/main/resources/E_V.txt");

        try (Scanner scanner = new Scanner(inputPath)) {
            int targetEndIndex = 0;
            while (scanner.hasNextLine()) {
                // Read each line from the file
                String line = scanner.nextLine();
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
        } catch (IOException e) {
            System.out.println("Error reading from the dictionary file");
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
        return word1.compareTo(word2);
    }


    // Method to add a new word to the dictionary
    public static boolean addWord(String target, String spelling, String definition) {
        // Create a new Word object with the provided parameters
        Word newWord = new Word(target, spelling, definition);

        // Retrieve the dictionary from the GUI
        List<Word> dict = DictionaryGUI.dict;

        // Find the correct position to insert newWord into the list in ascending order of target
        int indexToInsert = 0;
        while (indexToInsert < dict.size() && compareWords(dict.get(indexToInsert).getWord_target(), target) < 0) {
            indexToInsert++;
        }

        // Check for duplicates
        if (indexToInsert < dict.size() && compareWords(dict.get(indexToInsert).getWord_target(), target) == 0) {
            // If the target word already exists in the dictionary, return false (not added)
            return false;
        }

        // Insert newWord into the dictionary at the determined position
        dict.add(indexToInsert, newWord);

        // Return true to indicate that the word has been successfully added
        return true;
    }


    // Method to lookup a word in the dictionary and return its index
    public static int DictionaryGUILookup(String targetWord) {
        for (Word word : DictionaryGUI.dict) {
            if (word.getWord_target().equals(targetWord)) {
                return DictionaryGUI.dict.indexOf(word);
            }
        }
        return 0;
    }

    // Method to remove a word from the dictionary
    public static void removeWord(int inde) {
        if (inde >= 0 && inde < DictionaryGUI.dict.size()) {
            DictionaryGUI.dict.remove(inde);
        } else {
            System.out.println("Invalid");
        }
    }


    // Method to edit an existing word in the dictionary
    public static boolean editWord(Word editedWord) {
        for (Word word : DictionaryGUI.dict) {
            if (word.getWord_target().equals(editedWord.getWord_target())) {
                DictionaryGUI.dict.set(DictionaryGUI.dict.indexOf(word), editedWord);
                return true;
            }
        }
        return false;
    }


}
