package com.example.dictionaryApplication;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Speech {

    public static void speak(String word){
        if (word.equals("")) {
            return;
        }
        System.setProperty("freetts.voices","com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        // Voice[] voicelist = VoiceManager.getInstance().getVoices();
        if(voice!=null){
            voice.allocate();
            boolean status =voice.speak(word);
            voice.deallocate();
        }
        else {
            System.out.println("error");
        }
    }

}
