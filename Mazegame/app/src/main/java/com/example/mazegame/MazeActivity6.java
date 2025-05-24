package com.example.mazegame;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public class MazeActivity6 extends AppCompatActivity {

    private AlertDialog settingsDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze_level_6);
        startService(new Intent(this, MusicService.class));
        SystemUIHelper.hideSystemUI(this);
        ImageButton btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(v -> {
            ButtonSoundManager.playButtonClickSound(this);
            showSettingsDialog();
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        MusicService.setVolume(0.2f);
        MusicService.resumeMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicService.pauseMusic();
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);

        Typeface pixelFont = ResourcesCompat.getFont(this, R.font.pixel);

        TextView titleView = new TextView(this);
        titleView.setText("НАЛАШТУВАННЯ");
        titleView.setTextSize(30);
        titleView.setTypeface(pixelFont);
        titleView.setTextColor(Color.parseColor("#e6f7ff"));
        titleView.setGravity(Gravity.CENTER);
        titleView.setPadding(20, 20, 20, 20);

        LinearLayout mainContainer = new LinearLayout(this);
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.setGravity(Gravity.CENTER_HORIZONTAL);
        mainContainer.setPadding(40, 40, 40, 40);
        mainContainer.addView(titleView);

        float scale = getResources().getDisplayMetrics().density;
        int buttonHeightDp = 55;
        int marginPx = (int) (1 * scale);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (buttonHeightDp * scale)
        );
        buttonParams.setMargins(0, marginPx, 0, marginPx);

        ImageButton menuButton = new ImageButton(this);
        menuButton.setImageResource(R.drawable.menu_menu);
        menuButton.setBackgroundColor(Color.TRANSPARENT);
        menuButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        menuButton.setLayoutParams(buttonParams);
        menuButton.setOnClickListener(v -> {
            ButtonSoundManager.playButtonClickSound(this);
            settingsDialog.dismiss();
            Intent intent = new Intent(this, LevelSelectionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        ImageButton restartButton = new ImageButton(this);
        restartButton.setImageResource(R.drawable.menu_restart);
        restartButton.setBackgroundColor(Color.TRANSPARENT);
        restartButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        restartButton.setLayoutParams(buttonParams);
        restartButton.setOnClickListener(v -> {
            ButtonSoundManager.playButtonClickSound(this);
            settingsDialog.dismiss();
            recreate();
        });

        ImageButton musicToggleButton = new ImageButton(this);
        updateMusicButtonImage(musicToggleButton);
        musicToggleButton.setBackgroundColor(Color.TRANSPARENT);
        musicToggleButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        musicToggleButton.setLayoutParams(buttonParams);
        musicToggleButton.setOnClickListener(v -> {
            boolean current = MusicService.isMusicEnabled();
            MusicService.setMusicEnabled(this, !current);
            updateMusicButtonImage(musicToggleButton);
        });

        mainContainer.addView(menuButton);
        mainContainer.addView(restartButton);
        mainContainer.addView(musicToggleButton);

        builder.setView(mainContainer);
        builder.setCancelable(true);

        settingsDialog = builder.create();
        settingsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        settingsDialog.show();

        if (settingsDialog.getWindow() != null) {
            settingsDialog.getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.9),
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            settingsDialog.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }
    }

    private ImageButton createImageButton(int drawableResId, LinearLayout.LayoutParams params) {
        ImageButton button = new ImageButton(this);
        button.setImageResource(drawableResId);
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setScaleType(ImageView.ScaleType.FIT_CENTER);
        button.setAdjustViewBounds(true);
        button.setLayoutParams(params);
        return button;
    }

    private void updateMusicButtonImage(ImageButton button) {
        if (MusicService.isMusicEnabled()) {
            button.setImageResource(R.drawable.on_sound);
        } else {
            button.setImageResource(R.drawable.off_sound);
        }
    }
}
