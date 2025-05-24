package com.example.mazegame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class LevelSelectionActivity extends AppCompatActivity {

    private ImageButton soundToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);
        SystemUIHelper.hideSystemUI(this);
        ImageButton level1Button = findViewById(R.id.level1Button);
        startService(new Intent(this, MusicService.class));
        level1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelSelectionActivity.this, MazeActivity1.class);
                intent.putExtra("level", 1);
                startActivity(intent);
            }
        });

        ImageButton level2Button = findViewById(R.id.level2Button);
        level2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelSelectionActivity.this, MazeActivity2.class);
                intent.putExtra("level", 2);
                startActivity(intent);
            }
        });

        ImageButton level3Button = findViewById(R.id.level3Button);
        level3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelSelectionActivity.this, MazeActivity3.class);
                intent.putExtra("level", 3);
                startActivity(intent);
            }
        });

        ImageButton level4Button = findViewById(R.id.level4Button);
        level4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelSelectionActivity.this, MazeActivity4.class);
                intent.putExtra("level", 3);
                startActivity(intent);
            }
        });

        ImageButton level5Button = findViewById(R.id.level5Button);
        level5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelSelectionActivity.this, MazeActivity5.class);
                intent.putExtra("level", 3);
                startActivity(intent);
            }
        });

        ImageButton level6Button = findViewById(R.id.level6Button);
        level6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelSelectionActivity.this, MazeActivity6.class);
                intent.putExtra("level", 3);
                startActivity(intent);
            }
        });

        soundToggleButton = findViewById(R.id.soundToggleButton);

        // Оновлюємо іконку при старті
        updateSoundButtonIcon();

        soundToggleButton.setOnClickListener(v -> {
            boolean isMusicEnabled = MusicService.isMusicEnabled();
            MusicService.setMusicEnabled(this, !isMusicEnabled);
            updateSoundButtonIcon();
        });
    }
    private void updateSoundButtonIcon() {
        if (MusicService.isMusicEnabled()) {
            soundToggleButton.setImageResource(R.drawable.sound_on); // твоя іконка увімкненого звуку
        } else {
            soundToggleButton.setImageResource(R.drawable.sound_of); // твоя іконка вимкненого звуку
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MusicService.setVolume(0.5f);
        MusicService.resumeMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicService.pauseMusic();
    }
}
