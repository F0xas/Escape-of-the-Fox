package com.example.mazegame;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MazeView1 extends View {
    private final Paint exitPaint;

    private final MediaPlayer coinSound;
    private AlertDialog levelCompleteDialog;
    private final int[][] maze = {
            {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1},
            {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1}
    };

    private int cellSize;
    private int playerRow = 21, playerCol = 5;
    private final int exitRow = 0, exitCol = 5;
    private float startX, startY;
    private final Handler handler = new Handler();
    private final List<int[]> coins;
    private int collectedCoins = 0;

    private Bitmap playerBitmap;
    private Bitmap playerBlinkBitmap;
    private boolean isBlinking = false;
    private final int BLINK_TIME = 500;
    private Handler blinkHandler = new Handler();
    private Bitmap[] coinBitmaps;

    private Bitmap wallBitmap;
    private int coinFrame = 0;
    private final int ANIMATION_SPEED = 200;

    public MazeView1(Context context, AttributeSet attrs) {
        super(context, attrs);

        exitPaint = new Paint();
        exitPaint.setColor(0xFF00FF00);


        playerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player);
        playerBlinkBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.palyer1);
        coinBitmaps = new Bitmap[6];
        coinBitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.coin0);
        coinBitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.coin1);
        coinBitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.coin2);
        coinBitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.coin4);
        coinBitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.coin5);
        coinBitmaps[5] = BitmapFactory.decodeResource(getResources(), R.drawable.coin0);

        coins = new ArrayList<>();
        coins.add(new int[]{12, 7});
        coins.add(new int[]{9, 3});
        coins.add(new int[]{20, 1});

        animateCoin();
        coinSound = MediaPlayer.create(getContext(), R.raw.click_sound);

        wallBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wall_texture_1);
        showGameRules();
    }

    private void animateCoin() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                coinFrame++;
                if (coinFrame >= coinBitmaps.length) {
                    coinFrame = 0;
                }
                invalidate();

                animateCoin();
            }
        }, ANIMATION_SPEED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        cellSize = Math.min(width / maze[0].length, height / maze.length);

        float offsetX = (width - cellSize * maze[0].length) / 2;
        float offsetY = (height - cellSize * maze.length) / 2;

        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                float left = offsetX + col * cellSize;
                float top = offsetY + row * cellSize;
                float right = left + cellSize;
                float bottom = top + cellSize;

                if (maze[row][col] == 1) {
                    canvas.drawBitmap(wallBitmap, null, new RectF(left, top, right, bottom), null);
                } else if (maze[row][col] == 0) {

                }
            }
        }

        for (int[] coin : coins) {
            float cx = offsetX + coin[1] * cellSize + cellSize / 2;
            float cy = offsetY + coin[0] * cellSize + cellSize / 2;

            Bitmap currentCoinBitmap = coinBitmaps[coinFrame];

            Bitmap scaledCoinBitmap = Bitmap.createScaledBitmap(currentCoinBitmap, cellSize, cellSize, false);
            canvas.drawBitmap(scaledCoinBitmap, cx - scaledCoinBitmap.getWidth() / 2, cy - scaledCoinBitmap.getHeight() / 2, null);
        }

        float playerX = offsetX + playerCol * cellSize + cellSize / 2;
        float playerY = offsetY + playerRow * cellSize + cellSize / 2;
        Bitmap currentPlayerBitmap = isBlinking ? playerBlinkBitmap : playerBitmap;
        Bitmap scaledPlayerBitmap = Bitmap.createScaledBitmap(currentPlayerBitmap, cellSize, cellSize, false);
        canvas.drawBitmap(scaledPlayerBitmap, playerX - scaledPlayerBitmap.getWidth() / 2, playerY - scaledPlayerBitmap.getHeight() / 2, null);
    }

    private static final int SWIPE_THRESHOLD = 50;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                float endX = event.getX();
                float endY = event.getY();
                float dx = endX - startX;
                float dy = endY - startY;

                if (Math.abs(dx) > SWIPE_THRESHOLD || Math.abs(dy) > SWIPE_THRESHOLD) {
                    if (Math.abs(dx) > Math.abs(dy)) {
                        if (dx > 0) movePlayerSmoothly(1, 0);
                        else movePlayerSmoothly(-1, 0);
                    } else {
                        if (dy > 0) movePlayerSmoothly(0, 1);
                        else movePlayerSmoothly(0, -1);
                    }
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void showToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void movePlayerSmoothly(final int dx, final int dy) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int nextRow = playerRow + dy;
                int nextCol = playerCol + dx;
                if (nextRow >= 0 && nextRow < maze.length && nextCol >= 0 && nextCol < maze[0].length && maze[nextRow][nextCol] != 1) {
                    playerRow = nextRow;
                    playerCol = nextCol;

                    collectCoin();

                    if (playerRow == exitRow && playerCol == exitCol) {
                        showLevelCompleteDialog();
                    }
                    // Перевіряємо, чи гравець потрапив на пастку
                    if (maze[playerRow][playerCol] == 2) {
                        resetGame();
                        return;
                    }
                    invalidate();
                    movePlayerSmoothly(dx, dy);
                }
            }
        }, 5);
    }

    private void collectCoin() {
        for (int i = 0; i < coins.size(); i++) {
            if (coins.get(i)[0] == playerRow && coins.get(i)[1] == playerCol) {
                coins.remove(i);
                collectedCoins++;

                if (coinSound != null) {
                    coinSound.start();
                }
                isBlinking = true;
                invalidate();
                blinkHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isBlinking = false;
                        invalidate();
                    }
                }, BLINK_TIME);

                break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void showLevelCompleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);

        Typeface pixelFont = ResourcesCompat.getFont(getContext(), R.font.pixel);

        TextView titleView = new TextView(getContext());
        titleView.setText("Рівень пройдено!");
        titleView.setTextSize(24);
        titleView.setTypeface(pixelFont);
        titleView.setTextColor(Color.WHITE);
        titleView.setGravity(Gravity.CENTER);
        titleView.setPadding(20, 20, 20, 20);

        TextView messageView = new TextView(getContext());
        messageView.setText("Кількість зібраних монет: " + collectedCoins + "/3");
        messageView.setTextSize(18);
        messageView.setTypeface(pixelFont);
        messageView.setTextColor(Color.WHITE);
        messageView.setGravity(Gravity.CENTER);
        messageView.setPadding(20, 10, 20, 30);

        LinearLayout containerLayout = new LinearLayout(getContext());
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.setGravity(Gravity.CENTER);
        containerLayout.setPadding(20, 20, 20, 20);
        containerLayout.addView(titleView);
        containerLayout.addView(messageView);

        LinearLayout buttonLayout = new LinearLayout(getContext());
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);
        buttonLayout.setPadding(20, 20, 20, 20);

        int marginInDp = 2;
        float scale = getContext().getResources().getDisplayMetrics().density;
        int marginInPx = (int) (marginInDp * scale + 0.5f);
        int buttonHeightDp = 50;

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                (int) (buttonHeightDp * scale)
        );
        buttonParams.setMargins(marginInPx, 0, marginInPx, 0);

        ImageButton nextButton = new ImageButton(getContext());
        nextButton.setImageResource(R.drawable.next);
        nextButton.setBackgroundColor(Color.TRANSPARENT);
        nextButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        nextButton.setAdjustViewBounds(true);
        nextButton.setLayoutParams(buttonParams);
        nextButton.setOnClickListener(v -> {
            ButtonSoundManager.playButtonClickSound(getContext());
            levelCompleteDialog.dismiss();
            goToNextLevel();
        });

        ImageButton menuButton = new ImageButton(getContext());
        menuButton.setImageResource(R.drawable.menu);
        menuButton.setBackgroundColor(Color.TRANSPARENT);
        menuButton.setAdjustViewBounds(true);
        menuButton.setLayoutParams(buttonParams);
        menuButton.setOnClickListener(v -> {
            ButtonSoundManager.playButtonClickSound(getContext());
            levelCompleteDialog.dismiss();
            returnToMenu();
        });

        ImageButton restartButton = new ImageButton(getContext());
        restartButton.setImageResource(R.drawable.restart);
        restartButton.setBackgroundColor(Color.TRANSPARENT);
        restartButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        restartButton.setAdjustViewBounds(true);
        restartButton.setLayoutParams(buttonParams);
        restartButton.setOnClickListener(v -> {
            ButtonSoundManager.playButtonClickSound(getContext());
            levelCompleteDialog.dismiss();
            resetGame();
        });

        buttonLayout.addView(nextButton);
        buttonLayout.addView(menuButton);
        buttonLayout.addView(restartButton);
        containerLayout.addView(buttonLayout);

        builder.setView(containerLayout);
        builder.setCancelable(false);
        levelCompleteDialog = builder.create();
        levelCompleteDialog.show();
        if (levelCompleteDialog.getWindow() != null) {
            levelCompleteDialog.getWindow().setLayout(
                    dpToPx(400),
                    WindowManager.LayoutParams.WRAP_CONTENT
            );

            levelCompleteDialog.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }
    }


    private void goToNextLevel() {
        Context context = getContext();
        Intent intent = new Intent(context, MazeActivity4.class);
        context.startActivity(intent);

        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).finish();
        }
    }

    private void returnToMenu() {
        Context context = getContext();
        Intent intent = new Intent(context, LevelSelectionActivity.class);
        context.startActivity(intent);

        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).finish();
        }
    }

    private void resetGame() {
        playerRow = 21;
        playerCol = 5;
        collectedCoins = 0;
        coins.clear();
        coins.add(new int[]{12, 7});
        coins.add(new int[]{9, 3});
        coins.add(new int[]{20, 1});

        if (levelCompleteDialog != null && levelCompleteDialog.isShowing()) {
            levelCompleteDialog.dismiss();
        }

        invalidate();
    }
    private int dpToPx(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void showGameRules() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);

        Typeface pixelFont = ResourcesCompat.getFont(getContext(), R.font.pixel);

        TextView titleView = new TextView(getContext());
        titleView.setText("Правила гри:");
        titleView.setTextSize(24);
        titleView.setTypeface(pixelFont);
        titleView.setTextColor(Color.WHITE);
        titleView.setGravity(Gravity.CENTER);
        titleView.setPadding(20, 20, 20, 20);

        TextView messageView = new TextView(getContext());
        messageView.setText("🕹️ Твоя місія — пройти лабіринт!\n\n\n" +
                "Свайпай щоб рухатись та збирай монети на шляху.\n\n\n" +
                "Дійди до виходу, щоб перемогти!");
        messageView.setTextSize(18);
        messageView.setTypeface(pixelFont);
        messageView.setTextColor(Color.WHITE);
        messageView.setGravity(Gravity.CENTER);
        messageView.setPadding(20, 10, 20, 30);

        ImageButton closeButton = new ImageButton(getContext());
        closeButton.setImageResource(R.drawable.start_game);
        closeButton.setBackgroundColor(Color.TRANSPARENT);
        closeButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                dpToPx(250),
                dpToPx(80)
        );
        params.gravity = Gravity.CENTER;
        closeButton.setLayoutParams(params);

        closeButton.setPadding(10, 10, 10, 10);

        closeButton.setOnClickListener(v -> {
            ButtonSoundManager.playButtonClickSound(getContext());
            levelCompleteDialog.dismiss();
            resetGame();
        });

        LinearLayout containerLayout = new LinearLayout(getContext());
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.setGravity(Gravity.CENTER);
        containerLayout.setPadding(20, 20, 20, 20);
        containerLayout.addView(titleView);
        containerLayout.addView(messageView);
        containerLayout.addView(closeButton);

        builder.setView(containerLayout);
        builder.setCancelable(false);
        levelCompleteDialog = builder.create();
        levelCompleteDialog.show();
        if (levelCompleteDialog.getWindow() != null) {
            levelCompleteDialog.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }
    }

}