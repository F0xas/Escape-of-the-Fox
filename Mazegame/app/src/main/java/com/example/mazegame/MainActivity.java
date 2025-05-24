package com.example.mazegame;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mazegame.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemUIHelper.hideSystemUI(this);
        ImageButton btnStart = findViewById(R.id.btnStart);
        startService(new Intent(this, MusicService.class));
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonSoundManager.playButtonClickSound(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, LevelSelectionActivity.class);
                startActivity(intent);
            }
        });
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
