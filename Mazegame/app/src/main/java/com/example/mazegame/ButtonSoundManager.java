package com.example.mazegame;

import android.content.Context;
import android.media.MediaPlayer;

public class ButtonSoundManager {

    private static MediaPlayer mediaPlayer;

    public static void playButtonClickSound(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.click_sound);
        }
        mediaPlayer.start();
    }

    public static void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
