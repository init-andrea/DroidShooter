package unipg.pigdm.droidshooter.view.uicontroller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import unipg.pigdm.droidshooter.R;
import unipg.pigdm.droidshooter.logic.Enemy;
import unipg.pigdm.droidshooter.logic.GameState;
import unipg.pigdm.droidshooter.sound.SoundPlayer;

public class PauseScreenActivity extends AppCompatActivity {
    private TextView score;
    private ImageButton audioButton;
    private Button resumeButton;
    private Button quitButton;
    private Parcel gameStateParcel;
    private GameState gameState;
    private ArrayList<Enemy> enemies;
    private SoundPlayer soundPlayer;

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

        gameState = getIntent().getParcelableExtra("gameState");

        setContentView(R.layout.activity_pause);

        soundPlayer = new SoundPlayer(this);
        audioState = true;
        score = findViewById(R.id.scoreLabel);
        audioButton = findViewById(R.id.audioButton);
        resumeButton = findViewById(R.id.resumeButton);
        quitButton = findViewById(R.id.quitButton);

        resumeButton.setOnClickListener(resumeClickListener);
        quitButton.setOnClickListener(quitClickListener);
    }

    public void changeAudioState(View view) {
        audioState = !audioState;
        soundPlayer.playGenericButtonSound();
        if (audioState)
            audioButton.setImageResource(R.drawable.audio_on);
        else
            audioButton.setImageResource(R.drawable.audio_off);
    }

    public void resumeGame(View view) {
        Intent intent = new Intent(PauseScreenActivity.this, GameActivity.class);
        intent.putExtra("gameState", gameState);
        soundPlayer.playGenericButtonSound();
        startActivity(intent);
    }


    public void quitGame(View view){
        soundPlayer.playGenericButtonSound();
        finishAffinity();
        finish();
        System.exit(0);
    }
}
