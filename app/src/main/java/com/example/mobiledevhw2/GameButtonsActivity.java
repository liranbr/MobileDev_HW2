package com.example.mobiledevhw2;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.Random;


public class GameButtonsActivity extends AppCompatActivity {

    private GameManager GM;

    @Override
    protected void onPause() {
        GM.paused = true;
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_buttons);

        GM = new GameManager();
        GM.startGame(this);

        MaterialButton[] controls = { // order matters - UDLR is 0123 in the Direction enum
                findViewById(R.id.buttonUp),
                findViewById(R.id.buttonDown),
                findViewById(R.id.buttonLeft),
                findViewById(R.id.buttonRight)};
        for (int i = 0; i < controls.length; i++) {
            Character.Direction dir = Character.Direction.values()[i];
            controls[i].setOnClickListener(view -> GM.inputDirection(dir));
        }
    }
}