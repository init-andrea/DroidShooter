package unipg.pigdm.droidshooter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import unipg.pigdm.droidshooter.R;
import unipg.pigdm.droidshooter.model.Enemy;
import unipg.pigdm.droidshooter.model.GameState;

public class PauseScreenActivity extends AppCompatActivity {
    private TextView score;
    private ImageButton audioButton;
    private Button resumeButton;
    private Button quitButton;
    private Parcel gameStateParcel;
    private GameState gameState;
    private ArrayList<Enemy> enemies;

    private static boolean audioState;

    private View.OnClickListener resumeClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            resumeGame(v);
        }

    };

    private View.OnClickListener quitClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            quitGame(v);
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameState = (GameState) getIntent().getParcelableExtra("gameState");
        if (gameState == null)
            Log.d("null1", "null1");
        Log.d("enemiesPaused", String.valueOf(gameState.getEnemies().size()));
        Log.d("try", gameState.getScore() + " " + gameState.getTimeLeftInMillis());

        setContentView(R.layout.activity_pause);

        audioState = true;
        score = findViewById(R.id.scoreLabel);
        audioButton = findViewById(R.id.audioButton);
        resumeButton = findViewById(R.id.resumeButton);
        quitButton = findViewById(R.id.quitButton);

        score.setText(String.valueOf(gameState.getScore()));
        Log.d("score", (String) score.getText());
        //enemies = new ArrayList<>(gameState.getEnemies());
        //Log.d("pauseGameState", String.valueOf(enemies.size()));

        resumeButton.setOnClickListener(resumeClickListener);
        quitButton.setOnClickListener(quitClickListener);
    }

    public void changeAudioState(View view) {
        audioState = !audioState;
        if (audioState)
            audioButton.setImageResource(R.drawable.audio_on);
        else
            audioButton.setImageResource(R.drawable.audio_off);
    }

    public void resumeGame(View view) {
        Intent intent = new Intent(PauseScreenActivity.this, GameActivity.class);
        //Log.d("enemiesSizePause", String.valueOf(gameState.getEnemies().size()));
        if (gameState == null)
            Log.d("null2", "null2");
        intent.putExtra("gameState", gameState);
        if (gameState == null)
            Log.d("null3", "null3");
        startActivity(intent);
    }


    public void quitGame(View view){
        finishAffinity();
        finish();
        System.exit(0);
    }
}
