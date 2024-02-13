package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hw1.Logic.GameManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ShapeableImageView main_IMG_background;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView[][] main_IMG_road;
    private FloatingActionButton main_FAB_left;
    private FloatingActionButton main_FAB_right;
    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
        Glide
                .with(this)
                .load(R.drawable.road)
                .placeholder(R.drawable.ic_launcher_background)
                .into(this.main_IMG_background);

        this.gameManager = new GameManager(this.main_IMG_hearts.length, this.main_IMG_road.length, this.main_IMG_road[0].length);
        startTimer();
        refreshUI();
    }

    private void startTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> gameTick());
            }
        }, GameManager.DELAY, GameManager.DELAY);
    }

    private void gameTick() {
        // move the road
        vibrateAndToast(this.gameManager.moveRoad());
        if(gameManager.getLife() == 0)  // game over
            gameManager.reset();
        refreshUI();
    }

    private void initViews() {
        this.main_FAB_left.setOnClickListener(view -> moveCarClicked(GameManager.Directions.LEFT));
        this.main_FAB_right.setOnClickListener(view -> moveCarClicked(GameManager.Directions.RIGHT));
    }

    private void moveCarClicked(GameManager.Directions direction) {
        vibrateAndToast(this.gameManager.moveCar(direction));
        refreshUI();
    }

    private void vibrateAndToast(GameManager.GameStatus status){
        switch (status){
            case BLOCKED:
                vibrate(50);
                break;
            case CRASHED:
                vibrate(400);
                toast("You Crashed!");
                break;
            case GAME_OVER:
                vibrate(700);
                toast("GameOver, try again ><");
                break;
            case OK:
            default:
                break;
        }
    }

    private void vibrate(int milliseconds) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    private void toast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void refreshUI() {
        // refreshing the road
        for (int r = 0; r < this.main_IMG_road.length; r++) {
            for (int l = 0; l < this.main_IMG_road[0].length; l++)
                this. main_IMG_road[r][l].setImageResource(this.gameManager.getRoad()[r][l].getImage());
        }

        // refreshing the hearts
        for (int i = 0; i < this.main_IMG_hearts.length; i++) {
            int visibility = i < this.gameManager.getLife() ? View.VISIBLE : View.INVISIBLE;
            this.main_IMG_hearts[i].setVisibility(visibility);
        }
    }

    private void findViews() {
        // background
        this.main_IMG_background = findViewById(R.id.main_IMG_background);

        // hearts array
        LinearLayoutCompat heartsLLC = findViewById((R.id.main_LLC_hearts));
        int heartsCount = heartsLLC.getChildCount();
        this.main_IMG_hearts = new ShapeableImageView[heartsCount];
        for (int i = 0; i < heartsCount; i++)
            this.main_IMG_hearts[i] = (ShapeableImageView) heartsLLC.getChildAt(i);

        // road matrix
        LinearLayoutCompat roadLLC = findViewById((R.id.main_LLC_road));
        int lanes = roadLLC.getChildCount();
        LinearLayoutCompat[] lanesLLC = new LinearLayoutCompat[lanes];
        for (int i = 0; i < lanes; i++)
            lanesLLC[i] = ((LinearLayoutCompat) roadLLC.getChildAt(i));
        int rows = lanesLLC[0].getChildCount();
        this.main_IMG_road = new ShapeableImageView[rows][lanes];
        for (int r = 0; r < rows; r++) {
            for (int l = 0; l < lanes; l++)
                this.main_IMG_road[r][l] = (ShapeableImageView) lanesLLC[l].getChildAt(r);
        }

        // buttons
        this.main_FAB_left = findViewById(R.id.main_FAB_left);
        this.main_FAB_right = findViewById(R.id.main_FAB_right);
    }
}