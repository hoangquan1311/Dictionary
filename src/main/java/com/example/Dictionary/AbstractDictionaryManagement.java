package com.example.Dictionary;

import java.io.IOException;

public abstract class AbstractDictionaryManagement {

    // Abstract method to insert data from a file into the dictionary
    public abstract void insertDictGUIDataFromFile() throws IOException;

    // Abstract method to export dictionary data to a file
    public abstract void exportDictGUIDataToFile() throws IOException;

    // Abstract method to add a new word to the dictionary
    public abstract boolean addWord(String target, String spelling, String definition);

    // Abstract method to lookup a word in the dictionary and return its index
    public abstract int DictionaryGUILookup(String targetWord);

    // Abstract method to remove a word from the dictionary
    public abstract void removeWord(int index);

    // Abstract method to edit an existing word in the dictionary
    public abstract boolean editWord(Word editedWord);

    // Abstract method to compare two strings
    protected abstract int compareWords(String word1, String word2);
}