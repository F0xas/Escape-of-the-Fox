package com.example.mazegame;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

public class MusicService extends Service {

    private static MediaPlayer mediaPlayer;
    private static boolean isMusicEnabled = true; // за замовчуванням увімкнено

    @Override
    public void onCreate() {
        super.onCreate();
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
            mediaPlayer.setLooping(true);
        }
        loadMusicSetting();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void loadMusicSetting() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isMusicEnabled = prefs.getBoolean("music_enabled", true);
    }

    public static void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static void resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying() && isMusicEnabled) {
            mediaPlayer.start();
        }
    }

    public static void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }

    public static void setMusicEnabled(Context context, boolean enabled) {
        isMusicEnabled = enabled;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean("music_enabled", enabled).apply();

        if (mediaPlayer != null) {
            if (enabled) {
                mediaPlayer.start();
            } else {
                mediaPlayer.pause();
            }
        }
    }

    public static boolean isMusicEnabled() {
        return isMusicEnabled;
    }
}
