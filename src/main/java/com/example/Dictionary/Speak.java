package com.example.Dictionary;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Speak {

    public static void speak(String word) {
        if (isNullOrEmpty(word)) {
            return;
        }

        setupVoice();

        Voice voice = getVoice("kevin");

        if (voice != null) {
            processVoice(voice, word);
        } else {
            printErrorMessage();
        }
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private static void setupVoice() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
    }

    private static Voice getVoice(String voiceName) {
        return VoiceManager.getInstance().getVoice(voiceName);
    }

    private static void processVoice(Voice voice, String word) {
        voice.allocate();
        boolean status = voice.speak(word);
        voice.deallocate();
    }

    private static void printErrorMessage() {
        System.out.println("Lỗi");
    }
}
