package com.example.mobiledevhw2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        TextView playWithButtons = findViewById(R.id.main_BTN_gameButtons);
        TextView playWithSensors = findViewById(R.id.main_BTN_gameSensors);
        TextView leaderboard = findViewById(R.id.main_BTN_leaderboard);
        TextView quit = findViewById(R.id.main_BTN_quit);

        playWithButtons.setOnClickListener(view ->
                this.startActivity(new Intent(this, GameButtonsActivity.class)));

        playWithSensors.setOnClickListener(view ->
                this.startActivity(new Intent(this, GameSensorsActivity.class)));

        leaderboard.setOnClickListener(view ->
                this.startActivity(new Intent(this, LeaderboardActivity.class)));

        quit.setOnClickListener(view -> {
            finish();
            System.exit(0);
        });
    }
}
