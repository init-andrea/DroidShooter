package unipg.pigdm.droidshooter.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import unipg.pigdm.droidshooter.R;

public class StartGameActivity extends AppCompatActivity {

    private View.OnClickListener startClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            startGame(v);
        }

    };

    private View.OnClickListener quitClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            quitGame(v);
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
        setContentView(R.layout.activity_start);

        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = prefs.edit();

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
        //GameActivity game = new GameActivity();
        startActivity(intent);
        //game.gameStart();
    }

    public void quitGame(View view){
        finishAffinity();
        finish();
        System.exit(0);
    }

    public void openSettings(View view) {
        Intent intent = new Intent(StartGameActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}
