package unipg.pigdm.droidshooter.view.uicontroller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.Locale;
import java.util.Objects;

import unipg.pigdm.droidshooter.R;
import unipg.pigdm.droidshooter.logic.GameState;
import unipg.pigdm.droidshooter.logic.PlayerManager;
import unipg.pigdm.droidshooter.sound.SoundPlayer;
import unipg.pigdm.droidshooter.utilities.Utilities;
import unipg.pigdm.droidshooter.view.CustomGameView;

import static unipg.pigdm.droidshooter.utilities.Utilities.pxFromDp;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    //Default values
    private static final int TOTAL_HIGHSCORES_NUMBER = 9; // from 0 to 9, 10 total highscores are saved
    private static final int DEFAULT_ENEMIES_NUMBER = 5;
    private static final float DEFAULT_ENEMIES_SPEED = 5;
    private static final long DEFAULT_GAME_TIMER = 30;

    //Preferences management
    private SharedPreferences settingsPrefs;
    private SharedPreferences scorePrefs;
    SharedPreferences.Editor editor;

    //Values from preferences
    private static float enemySpeed;
    private static int enemyNumber;
    private static boolean showScore;
    private static boolean audioState;
    private static long gameTimer;

    //For the UI
    private static int score;
    private int prevScore;
    private TextView scoreText;
    private TextView textViewCountDown;

    //Crosshair
    private static float xPosition = 0.0f;
    private static float yPosition = 0.0f;

    //Sound
    private SoundPlayer soundPlayer;

    //State of the game
    private CountDownTimer countDownTimer;
    private PlayerManager playerManager;


    private long timeLeftInMillis;
    private CustomGameView customGameView;
    private static boolean gameWon;
    private static boolean gameResumed = false;
    private GameState gameState;

    //Sensor manager
    private SensorManager sensorManager = null;

    private View.OnClickListener pauseGameListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            pauseGame();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settingsPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        scorePrefs = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        getPreferences();

        playerManager = new PlayerManager();

        if (getIntent().getBooleanExtra("gameStarted", false) || getIntent().getBooleanExtra("gameRestarted", false)) {
            timeLeftInMillis = gameTimer;
            score = prevScore = 0;
            gameWon = false;
        }


        if (gameResumed) {
            gameState = getIntent().getParcelableExtra("gameState");
            assert gameState != null;
            CustomGameView.setGameState(gameState);
        }

        setContentView(R.layout.activity_game);

        soundPlayer = new SoundPlayer(this);

        customGameView = findViewById(R.id.customGameView);
        if (gameResumed & gameState != null) {
            customGameView.setEnemies(Utilities.arrayListFromString(gameState.getEnemies()));
            score = gameState.getScore();
            timeLeftInMillis = gameState.getTimeLeftInMillis();
            playerManager.setXPosition(gameState.getCrosshairXPosition());
            playerManager.setYPosition(gameState.getCrosshairYPosition());
            gameResumed = false;
        }

        scoreText = findViewById(R.id.scoreLabel);
        if (!showScore) {
            scoreText.setVisibility(View.INVISIBLE);
            scoreText.setHeight(0);
        }
        scoreText.setText(String.valueOf(score));

        textViewCountDown = findViewById(R.id.timerLabel);
        ImageButton pauseButton = findViewById(R.id.pauseButton);

        playerManager.setXMax(CustomGameView.getMaxWidth() - (float) pxFromDp(66));
        playerManager.setYMax(CustomGameView.getMaxHeight() - (float) pxFromDp(66));

        pauseButton.setOnClickListener(pauseGameListener);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gameStart();
    }

    //Gets user settings and checks for user input
    private void getPreferences() {
        enemySpeed = Utilities.checkUserInput(Objects.requireNonNull(settingsPrefs.getString("enemy_speed", String.valueOf(DEFAULT_ENEMIES_SPEED))), DEFAULT_ENEMIES_SPEED);
        enemyNumber = Utilities.checkUserInput(Objects.requireNonNull(settingsPrefs.getString("enemy_number", String.valueOf(DEFAULT_ENEMIES_NUMBER))), DEFAULT_ENEMIES_NUMBER);
        gameTimer = Utilities.checkUserInput(Objects.requireNonNull(settingsPrefs.getString("game_timer", String.valueOf(DEFAULT_GAME_TIMER))), DEFAULT_GAME_TIMER) * 1000;
        showScore = settingsPrefs.getBoolean("show_score", true);
        audioState = settingsPrefs.getBoolean("audio_state", true);
    }

    private void gameStart() {
        gameWon = false;
        countDownTimer = new CountDownTimer(timeLeftInMillis, 200) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                if (prevScore != score) {
                    scoreText.setText(String.valueOf(score));
                    prevScore = score;
                }
                if (gameWon) {
                    updateHighScores();
                    endGame();
                    countDownTimer.cancel();
                }
            }

            @Override
            public void onFinish() {
                updateHighScores();
                endGame();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeLeftFormatted);
    }

    private void updateHighScores() {
        editor = scorePrefs.edit();
        int possibleHighScore = 0;
        int possibleHighScoreIndex = TOTAL_HIGHSCORES_NUMBER + 1;
        for (int i = TOTAL_HIGHSCORES_NUMBER; i >= 0; i--) {
            if (scorePrefs.getInt("highscore" + i, 0) < score) {
                possibleHighScore = score;
                possibleHighScoreIndex = i;
            }
        }
        if (possibleHighScoreIndex != TOTAL_HIGHSCORES_NUMBER + 1)
            editor.putInt("highscore" + possibleHighScoreIndex, possibleHighScore);
        editor.apply();
    }

    private void pauseGame() {
        Intent intent = new Intent(GameActivity.this, PauseScreenActivity.class);
        countDownTimer.cancel();
        gameState = new GameState(customGameView.getEnemies(), score, timeLeftInMillis, xPosition, yPosition);
        intent.putExtra("gameState", gameState);
        customGameView.setPause();
        if (audioState)
            soundPlayer.playGenericButtonSound();
        startActivity(intent);
    }

    private void endGame() {
        Intent intent = new Intent(this, EndScreenActivity.class);
        intent.putExtra("won_value", gameWon);
        if (audioState) {
            if (gameWon)
                soundPlayer.playGameWonSound();
            else
                soundPlayer.playGameLostSound();
        }
        startActivity(intent);
        resetGame();
    }

    private void resetGame() {
        xPosition = yPosition = 0.0f;
        playerManager.resetCrosshair();
        getPreferences();
        gameResumed = false;
    }

    public static int getScore() {
        return score;
    }

    public static void updateScore(int points) {
        score += points;
    }

    public static void winGame() {
        gameWon = true;
    }

    public static float getCrosshairX() {
        return xPosition;
    }

    public static float getCrosshairY() {
        return yPosition;
    }

    public static float getEnemySpeed() {
        return enemySpeed;
    }

    public static int getEnemyNumber() {
        return enemyNumber;
    }

    //Sensor methods
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            playerManager.calculatePosition(sensorEvent.values[0], sensorEvent.values[1]);
        }

        xPosition = playerManager.getXPosition();
        yPosition = playerManager.getYPosition();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Do nothing
    }

    @Override
    protected void onPause() {
        // Unregister the sensor listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameResumed = true;
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

}