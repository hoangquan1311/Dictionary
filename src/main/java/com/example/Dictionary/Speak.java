package com.example.Dictionary;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

/**
 * The Speak class
 */
public class Speak {

    /**
     * Speaks the given word using the default "kevin" voice.
     *
     * @param word The word to be spoken.
     */
    public static void speak(String word) {
        // Check if the word is null or empty
        if (isNullOrEmpty(word)) {
            return;
        }

        // Set up the voice
        setupVoice();

        // Get the "kevin" voice
        Voice voice = getVoice("kevin");

        // Speak the word using the voice
        if (voice != null) {
            processVoice(voice, word);
        } else {
            // Print an error message if the voice is not available
            printErrorMessage();
        }
    }

    /**
     * Checks if a string is null or empty.
     *
     * @param str The string to be checked.
     * @return True if the string is null or empty, otherwise false.
     */
    private static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Sets up the FreeTTS voice by specifying the voice directory.
     */
    private static void setupVoice() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
    }

    /**
     * Retrieves a FreeTTS Voice instance based on the given voice name.
     *
     * @param voiceName The name of the desired voice.
     * @return A Voice instance corresponding to the specified voice name.
     */
    private static Voice getVoice(String voiceName) {
        return VoiceManager.getInstance().getVoice(voiceName);
    }

    /**
     * Allocates, speaks, and deallocates the voice for the given word.
     *
     * @param voice The Voice instance to be used for speaking.
     * @param word  The word to be spoken.
     */
    private static void processVoice(Voice voice, String word) {
        // Allocate the voice, speak the word, and deallocate the voice
        voice.allocate();
        boolean status = voice.speak(word);
        voice.deallocate();
    }

    /**
     * Prints an error message indicating a failure to obtain the specified voice.
     */
    private static void printErrorMessage() {
        System.out.println("Lỗi"); // Print an error message in Vietnamese
    }
}
