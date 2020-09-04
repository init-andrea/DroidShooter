package unipg.pigdm.droidshooter.view.uicontroller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import unipg.pigdm.droidshooter.R;
import unipg.pigdm.droidshooter.logic.GameState;
import unipg.pigdm.droidshooter.sound.SoundPlayer;

public class PauseScreenActivity extends AppCompatActivity {
    private ImageButton audioButton;
    private GameState gameState;
    private SoundPlayer soundPlayer;
    SharedPreferences.Editor editor;
    private SharedPreferences prefs;

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
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.activity_pause);

        soundPlayer = new SoundPlayer(this);
        audioState = prefs.getBoolean("audio_state", true);
        TextView score = findViewById(R.id.scoreLabel);
        audioButton = findViewById(R.id.audioButton);
        Button resumeButton = findViewById(R.id.resumeButton);
        Button quitButton = findViewById(R.id.quitButton);

        setAudioStateButton();

        resumeButton.setOnClickListener(resumeClickListener);
        quitButton.setOnClickListener(quitClickListener);
    }

    public void changeAudioState(View view) {
        if (audioState)
            soundPlayer.playGenericButtonSound();
        audioState = !audioState;
        editor = prefs.edit();
        editor.putBoolean("audio_state", audioState);
        editor.apply();
        setAudioStateButton();
    }

    private void setAudioStateButton() {
        if (audioState)
            audioButton.setImageResource(R.drawable.audio_on);
        else
            audioButton.setImageResource(R.drawable.audio_off);
    }

    public void resumeGame(View view) {
        Intent intent = new Intent(PauseScreenActivity.this, GameActivity.class);
        intent.putExtra("gameState", gameState);
        if (audioState)
            soundPlayer.playGenericButtonSound();
        startActivity(intent);
    }


    public void quitGame(View view) {
        if (audioState)
            soundPlayer.playGenericButtonSound();
        finishAffinity();
        finish();
        System.exit(0);
    }
}
