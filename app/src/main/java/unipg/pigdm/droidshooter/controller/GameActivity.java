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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Objects;
import java.util.Timer;

import unipg.pigdm.droidshooter.R;
import unipg.pigdm.droidshooter.model.EnemyManager;
import unipg.pigdm.droidshooter.view.CustomGameView;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private static int score = 0;
    private static float enemySpeed;
    private TextView scoreText;
    private static SharedPreferences prefs;

    private ImageButton pauseButton;

    private View.OnClickListener pauseGameListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            pauseGame(v);
        }

    };

    private Timer timer;
    final Handler handler = new Handler();
    private boolean play;
    
    //Images
    //private ImageView crosshair, cupcake, iceSandwich, kitKat, oreo;

    //Crosshair movement
    private static float xPosition, xAcceleration, xVelocity = 0.0f;
    private static float yPosition, yAcceleration, yVelocity = 0.0f;
    private static float xMax, yMax;

    private static final float XVELMAX = 4.0f;
    private static final float YVELMAX = 4.0f;

    private static final float FRAME_TIME =0.006f;

    //Sensor manager
    private SensorManager sensorManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        scoreText = findViewById(R.id.scoreLabel);

        //explosionImage = findViewById(R.id.explosion);
        //explosionImage.setBackgroundResource(R.drawable.explosion_animation);
        //explosionAnimation = (AnimationDrawable) explosionImage.getBackground();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        enemySpeed = getEnemySpeed();

        pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(pauseGameListener);

        xMax = CustomGameView.getMaxWidth() - (float)EnemyManager.pxFromDp(66);
        yMax = CustomGameView.getMaxHeight() - (float)EnemyManager.pxFromDp(66);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        play = true;
        timer = new Timer();
        Log.d("logging start", "game started"); //TODO
        /*timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (play) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            customGameView
                        }
                    })
                }
            }
        })*/
    }



    public static int getScore() {
        return score;
    }

    /*
    public void addScore(int points) {
        //TODO
        score += points;
        scoreText.setText(score);
    }
    */

    public void pauseGame(View view) {
        Intent intent = new Intent(GameActivity.this, PauseScreenActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //TODO
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
        return Float.parseFloat(Objects.requireNonNull(prefs.getString("enemy_speed", "5")));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Do nothing
    }

    @Override
    protected void onResume() {
        super.onResume();
        //enemySpeed = getEnemySpeed();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        // Unregister the sensor listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }

}