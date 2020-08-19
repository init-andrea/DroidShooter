package unipg.pigdm.droidshooter.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import unipg.pigdm.droidshooter.R;

public class EndScreenActivity extends AppCompatActivity {

    private TextView endText, endScore;
    private Button restartButton, menuButton;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean gameWon = getIntent().getBooleanExtra("won_value", false);
        setContentView(R.layout.activity_end);

        endScore = findViewById(R.id.endGameScore);
        endText = findViewById(R.id.endGameTextView);
        restartButton = findViewById(R.id.restartButton);
        menuButton = findViewById(R.id.menuButton);
        endScore.setText(String.valueOf(GameActivity.getScore()));
        setEndText(gameWon);

        restartButton.setOnClickListener(restartClickListener);
        menuButton.setOnClickListener(menuClickListener);
    }

    private void setEndText(boolean result) {
        if (result) {
            endText.setText(R.string.won_text);
        } else {
            endText.setText(R.string.lost_text);
        }
    }

    private void restartGame(View view) {

    }

    private void backToMenu(View view) {

    }

}
