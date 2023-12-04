package com.example.Dictionary;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

    public class DictionaryGUIManagement extends AbstractDictionaryManagement {


        // Implementing abstract method to insert data from a file into the dictionary
        @Override
        public void insertDictGUIDataFromFile() throws IOException {
            // Your implementation here...
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

        // Implementing abstract method to export dictionary data to a file
        @Override
        public void exportDictGUIDataToFile() throws IOException {
            // Your implementation here...
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

        // Implementing abstract method to add a new word to the dictionary
        @Override
        public boolean addWord(String target, String spelling, String definition) {
            // Your implementation here...
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

        // Implementing abstract method to lookup a word in the dictionary and return its index
        @Override
        public int DictionaryGUILookup(String targetWord) {
            // Your implementation here...
            for (Word word : DictionaryGUI.dict) {
                if (word.getWord_target().equals(targetWord)) {
                    return DictionaryGUI.dict.indexOf(word);
                }
            }
            return 0;
        }

        // Implementing abstract method to remove a word from the dictionary
        @Override
        public void removeWord(int index) {
            // Your implementation here...
            if (index >= 0 && index < DictionaryGUI.dict.size()) {
                DictionaryGUI.dict.remove(index);
            } else {
                System.out.println("Invalid");
            }
        }

        // Implementing abstract method to edit an existing word in the dictionary
        @Override
        public boolean editWord(Word editedWord) {
            // Your implementation here...
            for (Word word : DictionaryGUI.dict) {
                if (word.getWord_target().equals(editedWord.getWord_target())) {
                    DictionaryGUI.dict.set(DictionaryGUI.dict.indexOf(word), editedWord);
                    return true;
                }
            }
            return false;
        }

        // Implementing abstract method to compare two strings
        @Override
        protected int compareWords(String word1, String word2) {
            // Your implementation here...
            return word1.compareTo(word2);
        }
    }