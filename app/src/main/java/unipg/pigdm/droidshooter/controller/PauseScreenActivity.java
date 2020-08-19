package unipg.pigdm.droidshooter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import unipg.pigdm.droidshooter.R;

public class PauseScreenActivity extends AppCompatActivity {
    private TextView score;
    private ImageButton audioButton;
    private Button resumeButton;
    private Button quitButton;

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
        setContentView(R.layout.activity_pause);

        audioState = true;
        score = findViewById(R.id.scoreLabel);
        audioButton = findViewById(R.id.audioButton);
        resumeButton = findViewById(R.id.resumeButton);
        quitButton = findViewById(R.id.quitButton);

        String scoreString = "" + R.string.score_text + GameActivity.getScore();
        score.setText(scoreString);

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

    private void resumeGame(View view) {
        Intent intent = new Intent(PauseScreenActivity.this, GameActivity.class);
        startActivity(intent);
    }


    public void quitGame(View view){
        finishAffinity();
        finish();
        System.exit(0);
    }
}
