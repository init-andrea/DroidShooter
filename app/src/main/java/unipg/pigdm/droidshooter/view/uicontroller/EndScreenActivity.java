package unipg.pigdm.droidshooter.view.uicontroller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import unipg.pigdm.droidshooter.R;
import unipg.pigdm.droidshooter.sound.SoundPlayer;

public class EndScreenActivity extends AppCompatActivity {

    private TextView endText;
    private SoundPlayer soundPlayer;
    private boolean audioState;

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        audioState = prefs.getBoolean("audio_state", true);

        soundPlayer = new SoundPlayer(this);

        TextView endScore = findViewById(R.id.endGameScore);
        endText = findViewById(R.id.endGameTextView);
        Button restartButton = findViewById(R.id.restartButton);
        Button menuButton = findViewById(R.id.menuButton);
        Button quitButton = findViewById(R.id.endQuitButton);
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
        finishAffinity();
        Intent intent = new Intent(EndScreenActivity.this, GameActivity.class);
        intent.putExtra("gameRestarted", true);
        if (audioState)
            soundPlayer.playStartButtonSound();
        startActivity(intent);
    }

    private void backToMenu(View view) {
        Intent intent = new Intent(EndScreenActivity.this, StartGameActivity.class);
        if (audioState)
            soundPlayer.playGenericButtonSound();
        finishAffinity();
        startActivity(intent);
    }

    private void quitGame() {
        if (audioState)
            soundPlayer.playGenericButtonSound();
        finishAffinity();
        finish();
        System.exit(0);
    }

}
