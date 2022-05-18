package com.example.mobiledevhw2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Random;


public class GameSensorsActivity extends AppCompatActivity implements SensorEventListener {

    private GameManager GM;
    private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GM.paused = true;
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_sensors);

        GM = new GameManager();
        GM.startGame(this);

        ImageView arrow = findViewById(R.id.gameSensors_IMG_arrow);
        arrow.setVisibility(View.VISIBLE);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        ImageView arrow = findViewById(R.id.gameSensors_IMG_arrow);
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        if (x > (-2) && x < (2) && y > (-2) && y < (2)) {
            arrow.setImageResource(0);
        }
        else if (Math.abs(x) < Math.abs(y)) {
            if (y < 0) { // up
                GM.inputDirection(Character.Direction.UP);
                arrow.setImageResource(R.drawable.arrow_up);
                arrow.setScaleY(1);
            }
            if (y > 0) { // down
                GM.inputDirection(Character.Direction.DOWN);
                arrow.setImageResource(R.drawable.arrow_up);
                arrow.setScaleY(-1);
            }
        } else {
            if (x > 0) { // left
                GM.inputDirection(Character.Direction.LEFT);
                arrow.setImageResource(R.drawable.arrow_left);
                arrow.setScaleX(1);
            }
            if (x < 0) { // right
                GM.inputDirection(Character.Direction.RIGHT);
                arrow.setImageResource(R.drawable.arrow_left);
                arrow.setScaleX(-1);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void tester() {

    }
}