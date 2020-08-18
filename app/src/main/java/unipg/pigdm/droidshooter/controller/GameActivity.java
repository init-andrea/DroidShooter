package unipg.pigdm.droidshooter.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.Locale;
import java.util.Objects;

import unipg.pigdm.droidshooter.R;
import unipg.pigdm.droidshooter.model.EnemyManager;
import unipg.pigdm.droidshooter.view.CustomGameView;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private static int score = 0;
    private static float enemySpeed = R.string.default_enemies_speed;
    private static int enemyNumber = R.string.default_enemies_number;
    private static float gameTimer = R.string.default_timer_value;
    private static boolean showScore;
    private static boolean audioState;
    private TextView scoreText;
    private TextView textViewCountDown;

    private SharedPreferences settingsPrefs;

    private ImageButton pauseButton;

    private View.OnClickListener pauseGameListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            pauseGame(v);
        }

    };

    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private long timeLeftInMillis;


    private static boolean gameEnded;
    private static boolean gameWon;

    //Crosshair movement
    private static float xPosition, xAcceleration, xVelocity = 0.0f;
    private static float yPosition, yAcceleration, yVelocity = 0.0f;
    private static float xMax, yMax;

    private static final float FRAME_TIME =0.006f;

    //Sensor manager
    private SensorManager sensorManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settingsPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        getPreferences();

        timeLeftInMillis = (long)gameTimer;
        gameEnded = false;
        gameWon = false;
        score = 0;

        setContentView(R.layout.activity_game);

        Log.d("time", "gameT" + gameTimer + "timeL" + timeLeftInMillis);
        scoreText = findViewById(R.id.scoreLabel);
        textViewCountDown = findViewById(R.id.timerLabel);
        pauseButton = findViewById(R.id.pauseButton);
        scoreText.setText(R.string.score_text);

        xMax = CustomGameView.getMaxWidth() - (float)EnemyManager.pxFromDp(66);
        yMax = CustomGameView.getMaxHeight() - (float)EnemyManager.pxFromDp(66);

        /*
        if (!showScore) {
            scoreText.setVisibility(View.INVISIBLE);
            scoreText.setHeight(0);
        }
        */

        pauseButton.setOnClickListener(pauseGameListener);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gameStart();
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    /*
    public void explode(float x, float y) {
        explosionImage.setX(x);
        explosionImage.setY(y);
        explosionAnimation.start();
    }
    */

    public void gameStart() {
        gameWon = false;
        Log.d("logging start", "game started"); //TODO
        countDownTimer = new CountDownTimer(timeLeftInMillis, 200) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                Log.d("timeInTimer", String.valueOf(timeLeftInMillis));
                updateCountDownText();
                if (gameWon) {
                    timerRunning = false;
                    endGame();
                    countDownTimer.cancel();
                }
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                gameEnded = true;
                endGame();
            }
        }.start();
        timerRunning = true;

    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeLeftFormatted);
    }

    public void getPreferences() {
        enemySpeed = Float.parseFloat(Objects.requireNonNull(settingsPrefs.getString("enemy_speed", getString(R.string.default_enemies_speed))));
        enemyNumber = Integer.parseInt(Objects.requireNonNull(settingsPrefs.getString("enemy_number", getString(R.string.default_enemies_number))));
        gameTimer = Long.parseLong(Objects.requireNonNull(settingsPrefs.getString("game_timer", getString(R.string.default_timer_value)))) * 1000;
        showScore = settingsPrefs.getBoolean("show_score", true);
        audioState = settingsPrefs.getBoolean("audio_state", true);
    }

    public static int getScore() {
        return score;
    }

    /*
    public void updateScore(int points) {
        //TODO
        score += points;
        scoreText.setText(score);
    }
    */

    public void pauseGame(View view) {
        Intent intent = new Intent(GameActivity.this, PauseScreenActivity.class);
        countDownTimer.cancel();
        startActivity(intent);
    }

    public static void winGame() {
        gameWon = true;
        gameEnded = true;
    }

    public void endGame() {
        Intent intent = new Intent(this, EndScreenActivity.class);
        intent.putExtra("won_value", gameWon);
        startActivity(intent);
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
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

}