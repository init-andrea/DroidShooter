package unipg.pigdm.droidshooter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import unipg.pigdm.droidshooter.R;

public class EndScreenActivity extends AppCompatActivity {

    private TextView endText, endScore;
    private Button restartButton, menuButton, quitButton;

    private View.OnClickListener restartClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            restartGame(v);
        }

    };

    private View.OnClickListener menuClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            backToMenu(v);
        }

    };

    private View.OnClickListener quitClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            quitGame();
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean gameWon = getIntent().getBooleanExtra("won_value", false);
        setContentView(R.layout.activity_end);

        endScore = findViewById(R.id.endGameScore);
        endText = findViewById(R.id.endGameTextView);
        restartButton = findViewById(R.id.restartButton);
        menuButton = findViewById(R.id.menuButton);
        quitButton = findViewById(R.id.endQuitButton);
        endScore.setText(String.valueOf(GameActivity.getScore()));
        setEndText(gameWon);

        restartButton.setOnClickListener(restartClickListener);
        menuButton.setOnClickListener(menuClickListener);
        quitButton.setOnClickListener(quitClickListener);
    }

    private void setEndText(boolean result) {
        if (result) {
            endText.setText(R.string.won_text);
        } else {
            endText.setText(R.string.lost_text);
        }
    }

    private void restartGame(View view) {
        Intent intent = new Intent(EndScreenActivity.this, GameActivity.class);
        startActivity(intent);
    }

    private void backToMenu(View view) {
        Intent intent = new Intent(EndScreenActivity.this, StartGameActivity.class);
        startActivity(intent);
    }

    private void quitGame() {
        finishAffinity();
        finish();
        System.exit(0);
    }

}
