package com.example.Dictionary;

// A simple class representing a word with its target, spelling, and explanation
public class Word {
    // Fields to store the target word, its spelling, and explanation
    private String Word_target;
    private String Word_spell;
    private String Word_explain;

    // Constructor to initialize a Word object with target, spelling, and explanation
    public Word(String Word_target, String Word_spell, String Word_explain) {
        this.Word_target = Word_target;
        this.Word_spell = Word_spell;
        this.Word_explain = Word_explain;
    }

    // Getter method to retrieve the target word
    public String getWord_target() {
        return Word_target;
    }

    // Setter method to set the target word
    public void setWord_target(String word_target) {
        this.Word_target = word_target;
    }

    // Getter method to retrieve the spelling of the word
    public String getWord_spelling() {
        return Word_spell;
    }

    // Setter method to set the spelling of the word
    public void setWord_spelling(String word_spelling) {
        this.Word_spell = word_spelling;
    }

    // Setter method to set the explanation of the word
    public void setWord_explain(String word_explain) {
        this.Word_explain = word_explain;
    }

    // Getter method to retrieve the explanation of the word
    public String getWord_explain() {
        return Word_explain;
    }
}
