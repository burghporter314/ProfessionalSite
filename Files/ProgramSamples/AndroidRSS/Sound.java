package com.example.dylanporter.rssfeedbydylanporter;

/**
 * Created by Dylan Porter on 8/16/2016.
 */
public class Sound {

    private static int sound = 1;
    public void updateSound() {
        if(sound == 1) {
            sound = 0;
        } else {
            sound = 1;
        }
    }
    public int returnSound() {
        return this.sound;
    }
}
