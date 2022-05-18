package com.example.mobiledevhw2;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import java.util.Random;

public class GameManager {

    private final int ROWS = 8, COLS = 5, MAX_HEALTH = 3, DASH_COOLDOWN_DURATION = 4, COIN_SCORE = 10;
    private final float DELAY_SECONDS = 1f;
    private final int PLAYER_START_ROW = 5, PLAYER_START_COL = 2;
    private final int ENEMY_START_ROW = 2, ENEMY_START_COL = 2;

    private ImageView[][] grid;
    private ImageView[] hearts;
    private Character player;
    private Character enemy;
    private Character coin;
    private int HP; // Heart Points
    private int score;
    public boolean gameOver, paused;

    private Activity gameActivity;
    private MaterialButton actionButton;
    private int dashCooldown = 0;

    public void startGame(Activity gameActivity) {
        this.gameActivity = gameActivity;
        actionButton = gameActivity.findViewById(R.id.buttonAction);
        makeGrid(ROWS, COLS);
        player = new Character(grid, PLAYER_START_ROW, PLAYER_START_COL,
                R.drawable.madeline_up,
                R.drawable.madeline_left);
        enemy = new Character(grid, ENEMY_START_ROW, ENEMY_START_COL,
                R.drawable.oshiro_up,
                R.drawable.oshiro_left);
        coin = new Character(grid, 0, 0,
                R.drawable.strawberry,
                R.drawable.strawberry);
        replaceCoin();
        HP = MAX_HEALTH;
        fillHearts(HP);
        score = 0;
        writeScore(score);
        randomEnemyDirection();
        paused = true; // wait for first input
        actionButton.setOnClickListener(view -> {
            if (!paused && !gameOver && dashCooldown == 0) {
                MediaPlayer.create(gameActivity, R.raw.dash).start();
                player.move();
                boolean collided = checkCollision();
                if (!collided) {
                    player.move();
                    collided = checkCollision();
                }
                if (!collided) {
                    dashCooldown = DASH_COOLDOWN_DURATION;
                    actionButton.setBackgroundColor(Color.parseColor("#202124"));
                    actionButton.setText("DASH".substring(0, 4 - DASH_COOLDOWN_DURATION));
                    actionButton.setClickable(false);
                }
            }
        });

        // Start running game logic
        Handler clock = new Handler();
        clock.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!paused)
                    gameTick();
                if (!gameOver)
                    clock.postDelayed(this, (long) (DELAY_SECONDS * 1000));
            }
        }, 1000);
    }

    void gameTick() {
        writeScore(++score);
        if (dashCooldown > 0) {
            dashCooldown--;
            actionButton.setText("DASH".substring(0, 4 - dashCooldown));
            if (dashCooldown == 0) {
                actionButton.setBackgroundColor(Color.WHITE);
                actionButton.setClickable(true);
            }
        }
        player.move();
        boolean collided = checkCollision();
        if (!collided) { // if haven't collided yet,
            enemy.move(); // move the enemy, and check again
            collided = checkCollision();
        }
        if (!collided)
            randomEnemyDirection();
    }

    boolean checkCollision() {
        checkCoin();
        if (player.row == enemy.row && player.col == enemy.col) {
            randomEnemyDirection();
            MediaPlayer.create(gameActivity, R.raw.death).start();
            HP--;
            hearts[HP].setVisibility(View.INVISIBLE);
            paused = true;
            if (HP > 0) {
                player.setPosition(PLAYER_START_ROW, PLAYER_START_COL);
                enemy.setPosition(ENEMY_START_ROW, ENEMY_START_COL);
                replaceCoin();
            } else if (!gameOver) {
                gameOver = true;
                actionButton.setText("MAIN MENU");
                actionButton.setLines(2);
                actionButton.setBackgroundColor(Color.RED);
                actionButton.setTextColor(Color.WHITE);
                actionButton.setOnClickListener(view -> {
                    LeaderboardScore.addScoreToLeaderboard(gameActivity, score);
                    gameActivity.finish();
                });
            }
            return true;
        }
        else
            return false;
    }

    void inputDirection(Character.Direction direction) {
        if (!gameOver) {
            paused = false;
            player.setDirection(direction);
        }
    }

    void randomEnemyDirection() {
        int enemyDirection = new Random().nextInt(4);
        enemy.setDirection(Character.Direction.values()[enemyDirection]);
    }

    void checkCoin() {
        if (coin.row == player.row && coin.col == player.col) {
            score += COIN_SCORE;
            writeScore(score);
            MediaPlayer.create(gameActivity, R.raw.strawberry_get).start();
            replaceCoin();
        }
        if (coin.row == enemy.row && coin.col == enemy.col) {
            replaceCoin();
        }
    }

    void replaceCoin() {
        if (!((coin.row == player.row && coin.col == player.col) || (coin.row == enemy.row && coin.col == enemy.col)))
            grid[coin.row][coin.col].setImageResource(0);
        do {
            coin.row = new Random().nextInt(ROWS);
            coin.col = new Random().nextInt(COLS);
        } while ((coin.row == player.row && coin.col == player.col) || (coin.row == enemy.row && coin.col == enemy.col));
        grid[coin.row][coin.col].setImageResource(coin.spriteUp);
        grid[coin.row][coin.col].setScaleX(1);
        grid[coin.row][coin.col].setScaleY(1);
    }

    void makeGrid(final int rows, final int cols) {
        LinearLayout gridLayout = gameActivity.findViewById(R.id.game_LLO_grid);
        gridLayout.removeAllViews();
        grid = new ImageView[rows][cols];
        LinearLayout[] rowLayouts = new LinearLayout[rows];

        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
        LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        for (int i = 0; i < rows; i++) {
            rowLayouts[i] = new LinearLayout(gameActivity);
            rowLayouts[i].setLayoutParams(rowParams);
            rowLayouts[i].setOrientation(LinearLayout.HORIZONTAL);
            gridLayout.addView(rowLayouts[i]);
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new ImageView(gameActivity);
                grid[i][j].setLayoutParams(cellParams);
                grid[i][j].setBackgroundResource(R.drawable.grid_cell_background);
                rowLayouts[i].addView(grid[i][j]);
            }
        }
    }

    void fillHearts(final int maxHealth) {
        LinearLayout healthbar = gameActivity.findViewById(R.id.game_LLO_healthbar);
        healthbar.removeAllViews();
        hearts = new ImageView[maxHealth];
        for (int i = 0; i < maxHealth; i++) {
            hearts[i] = new ImageView(gameActivity);
            hearts[i].setImageResource(R.drawable.crystal_heart);
            hearts[i].setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            int pad = 20 - 3 * maxHealth; // to make it look ok for a maxHealth up to ~8
            hearts[i].setPadding(pad, pad, pad, pad);
            healthbar.addView(hearts[i]);
        }
    }

    void writeScore(int score) {
        TextView scoreTXT = gameActivity.findViewById(R.id.game_TXT_score);
        String s = "SCORE: " + score;
        scoreTXT.setText(s);
    }
}
