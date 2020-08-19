package unipg.pigdm.droidshooter.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import unipg.pigdm.droidshooter.R;

public class StartGameActivity extends AppCompatActivity {

    private TextView highScoreText;
    private String highScore;
    private SharedPreferences prefs;

    private View.OnClickListener startClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            startGame(v);
        }

    };

    private View.OnClickListener quitClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            quitGame();
        }

    };

    private View.OnClickListener settingsClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            openSettings(v);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        highScore = "All time HighScore: " + prefs.getInt("highscore0", 0);

        setContentView(R.layout.activity_start);

        highScoreText = findViewById(R.id.highScore);

        highScoreText.setText(String.valueOf(highScore));

        Button startButton = findViewById(R.id.startButton);
        Button quitButton = findViewById(R.id.quitButton);
        Button settingsButton = findViewById(R.id.settingsButton);

        startButton.setOnClickListener(startClickListener);
        quitButton.setOnClickListener(quitClickListener);
        settingsButton.setOnClickListener(settingsClickListener);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void startGame(View view) {
        Intent intent = new Intent(StartGameActivity.this, GameActivity.class);
        startActivity(intent);
    }

    public void quitGame() {
        finishAffinity();
        finish();
        System.exit(0);
    }

    public void openSettings(View view) {
        Intent intent = new Intent(StartGameActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}
