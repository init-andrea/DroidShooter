package unipg.pigdm.droidshooter.controller;

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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import unipg.pigdm.droidshooter.R;
import unipg.pigdm.droidshooter.model.GameState;
import unipg.pigdm.droidshooter.util.Utilities;
import unipg.pigdm.droidshooter.view.CustomGameView;

import static unipg.pigdm.droidshooter.util.Utilities.pxFromDp;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private static final int TOTAL_HIGHSCORES_NUMBER = 9; // from 0 to 9, 10 total highscores
    private static final int DEFAULT_ENEMIES_NUMBER = 5;
    private static final float DEFAULT_ENEMIES_SPEED = 5;
    private static final long DEFAULT_GAME_TIMER = 30;


    private static int score;
    private int prevScore;
    private static final float FRAME_TIME = 0.006f;
    private static float enemySpeed;
    private static int enemyNumber;
    private static boolean showScore;
    private static boolean audioState;
    private static long gameTimer;
    private TextView scoreText;
    private TextView textViewCountDown;
    private static boolean gameResumed = false;

    private SharedPreferences settingsPrefs;
    private SharedPreferences scorePrefs;
    SharedPreferences.Editor editor;

    private View.OnClickListener pauseGameListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            pauseGame(v);
        }

    };

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private static boolean gameEnded;
    private static boolean gameWon;
    private GameState gameState;

    //Crosshair movement
    private static float xPosition, xAcceleration, xVelocity = 0.0f;
    private static float yPosition, yAcceleration, yVelocity = 0.0f;
    private static float xMax, yMax;
    private CustomGameView customGameView;

    //Sensor manager
    private SensorManager sensorManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settingsPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        scorePrefs = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        getPreferences();
        if (!gameResumed) {
            timeLeftInMillis = gameTimer;
            score = prevScore = 0;
        }

        gameEnded = false;
        gameWon = false;

        customGameView = new CustomGameView(this);

        setContentView(R.layout.activity_game);

        scoreText = findViewById(R.id.scoreLabel);
        if (!showScore) {
            scoreText.setVisibility(View.INVISIBLE);
            scoreText.setHeight(0);
        }
        scoreText.setText(String.valueOf(score));

        textViewCountDown = findViewById(R.id.timerLabel);
        ImageButton pauseButton = findViewById(R.id.pauseButton);

        xMax = CustomGameView.getMaxWidth() - (float) pxFromDp(66);
        yMax = CustomGameView.getMaxHeight() - (float) pxFromDp(66);

        pauseButton.setOnClickListener(pauseGameListener);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getIntent().getBooleanExtra("gameStarted", false))
            gameStart();
        if (gameResumed)
            gameResumed = false;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    /*
    public void explode(float x, float y) {
        explosionImage.setX(x);
        explosionImage.setY(y);
        explosionAnimation.start();
    }
    */

    private void gameStart() {
        gameWon = false;
        //TODO
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
                gameEnded = true;
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

    //Gets user settings and checks for user input
    private void getPreferences() {
        enemySpeed = Utilities.checkUserInput(Objects.requireNonNull(settingsPrefs.getString("enemy_speed", String.valueOf(DEFAULT_ENEMIES_SPEED))), DEFAULT_ENEMIES_SPEED);
        enemyNumber = Utilities.checkUserInput(Objects.requireNonNull(settingsPrefs.getString("enemy_number", String.valueOf(DEFAULT_ENEMIES_NUMBER))), DEFAULT_ENEMIES_NUMBER);
        gameTimer = Utilities.checkUserInput(Objects.requireNonNull(settingsPrefs.getString("game_timer", String.valueOf(DEFAULT_GAME_TIMER))), DEFAULT_GAME_TIMER) * 1000;
        showScore = settingsPrefs.getBoolean("show_score", true);
        audioState = settingsPrefs.getBoolean("audio_state", true);
    }

    public static int getScore() {
        return score;
    }

    public static void updateScore(int points) {
        //TODO
        score += points;
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
            //Log.d("highscore" + i, String.valueOf(scorePrefs.getInt("highscore" + i, 0)));
        }
        if (possibleHighScoreIndex != TOTAL_HIGHSCORES_NUMBER + 1)
            editor.putInt("highscore" + possibleHighScoreIndex, possibleHighScore);
        editor.apply();
    }

    private void pauseGame(View view) {
        Intent intent = new Intent(GameActivity.this, PauseScreenActivity.class);
        countDownTimer.cancel();
        //gameResumed = false;
        //intent.putExtra("score", score);
        if (gameState != null && gameState.getEnemies() != null)
            gameState.getEnemies().clear();
        gameState = new GameState(new ArrayList<>(customGameView.getEnemies()), score, timeLeftInMillis, xPosition, yPosition);
        Log.d("getEnemies", String.valueOf(customGameView.getEnemies().size()));
        Log.d("enemiesGameState", String.valueOf(gameState.getEnemies().size()));
        intent.putExtra("gameState", gameState);
        startActivity(intent);
    }

    private void resumeGame() {

    }

    public static void winGame() {
        gameWon = true;
        gameEnded = true;
    }

    private void endGame() {
        Intent intent = new Intent(this, EndScreenActivity.class);
        intent.putExtra("won_value", gameWon);
        startActivity(intent);
        resetCrosshair();
    }

    private void resetCrosshair() {
        xPosition = xAcceleration = xVelocity = yPosition = yAcceleration = yVelocity = 0.0f;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAcceleration = sensorEvent.values[0];
            yAcceleration = sensorEvent.values[1];
        }

        //Calculate new speed
        xVelocity += (xAcceleration * FRAME_TIME);
        yVelocity += (yAcceleration * FRAME_TIME);

        //Calculate distance travelled in the time frame
        float xS = xVelocity + (xAcceleration / 2.0f) * FRAME_TIME * FRAME_TIME;
        float yS = yVelocity + (yAcceleration / 2.0f) * FRAME_TIME * FRAME_TIME;

        //Add to position negative for x value because sensor reads the opposite of what we want
        xPosition -= xS;
        yPosition += yS;

        checkMaxXY();

    }

    //Check if X or Y coordinates exceed the screen size and if the crosshair is on a side resets the velocity
    private void checkMaxXY() {
        if (xPosition > xMax) {
            xPosition = xMax;
            xVelocity = 0;
        } else if (xPosition < 0) {
            xPosition = 0;
            xVelocity = 0;
        }
        if (yPosition > yMax) {
            yPosition = yMax;
            yVelocity = 0;
        } else if (yPosition < 0) {
            yPosition = 0;
            yVelocity = 0;
        }
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
        gameState = (GameState) getIntent().getParcelableExtra("gameState");
        if (gameState != null) {
            score = gameState.getScore();
            Log.d("gameState", String.valueOf(gameState.getScore()));
            Log.d("size", String.valueOf(gameState.getEnemies().size()));
            customGameView.setEnemies(gameState.getEnemies());
            timeLeftInMillis = gameState.getTimeLeftInMillis();
            xPosition = gameState.getCrosshairXPosition();
            yPosition = gameState.getCrosshairYPosition();
            gameStart();
        }

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

}