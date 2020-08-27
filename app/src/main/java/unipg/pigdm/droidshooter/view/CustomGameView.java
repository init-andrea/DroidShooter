package unipg.pigdm.droidshooter.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;

import unipg.pigdm.droidshooter.R;
import unipg.pigdm.droidshooter.logic.Enemy;
import unipg.pigdm.droidshooter.logic.EnemyManager;
import unipg.pigdm.droidshooter.logic.GameState;
import unipg.pigdm.droidshooter.sound.SoundPlayer;
import unipg.pigdm.droidshooter.view.uicontroller.GameActivity;

import static unipg.pigdm.droidshooter.util.Utilities.getResizedImage;
import static unipg.pigdm.droidshooter.util.Utilities.pxFromDp;

public class CustomGameView extends View {

    private static final int CROSSHAIR_SIZE_DP = 66;

    private Bitmap crosshair;
    private Bitmap explosion;
    private static String enemiesState;
    private int aliveEnemies;
    private Bitmap cupcake, iceSandwich, kitKat, oreo;
    private int enemiesSize;
    private boolean gameResumed;
    private ArrayList<Enemy> enemies = null;
    private SoundPlayer soundPlayer;
    private SharedPreferences prefs;
    private boolean audioState;

    private EnemyManager enemyManager;

    //Display
    private static float density, width, height;

    public CustomGameView(Context context) {
        super(context);

        init(null);
    }

    public static float getDensity() {
        return density;
    }

    public static float getMaxWidth() {
        return width;
    }

    public static float getMaxHeight() {
        return height;
    }

    public CustomGameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public CustomGameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomGameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    public static void setGameState(GameState gameState) {
        enemiesState = gameState.getEnemies();
    }

    private void init(@Nullable AttributeSet set) {
        //TODO
        density = getResources().getDisplayMetrics().density;
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
        soundPlayer = new SoundPlayer(this.getContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        audioState = prefs.getBoolean("audio_state", true);

        if (enemiesState != null) {
            enemies = GameState.arrayListFromString(enemiesState);
        }

        enemyManager = new EnemyManager(enemies);
        if (!gameResumed)
            enemies = new ArrayList<>(enemyManager.getEnemiesList());

        enemiesSize = enemies.get(0).getSize();
        aliveEnemies = enemyManager.getAliveEnemies();
        loadBitmaps();
        resizeBitmaps();

    }

    @Override
    protected void onDraw(final Canvas canvas) {

        canvas.drawColor(Color.WHITE);

        //check if enemies are hit
        for (final Enemy enemy : enemyManager.getEnemiesList()) {
            if (enemyManager.isHit(enemy, GameActivity.getCrosshairX(), GameActivity.getCrosshairY(), CROSSHAIR_SIZE_DP, density) && !enemy.isDead()) {
                canvas.drawBitmap(explosion, enemy.getXPosition(), enemy.getYPosition(), null);
                if (audioState)
                    soundPlayer.playHitSound();
                enemy.setDead(true);
                aliveEnemies--;

                GameActivity.updateScore(enemy.getPoints());
            }
        }

        //TODO add list of dead enemies to draw the image on screen with countdowntimer(?)

        if (aliveEnemies == 0) {
            GameActivity.winGame();
        }


        //loop through list of alive enemies to draw them
        //Log.d("beforeDrawing",enemyManager.getEnemiesList().toString());
        for (Enemy enemy : enemyManager.getEnemiesList()) {
            if (enemy.isAlive()) {
                enemyManager.moveEnemy(enemy);
                switch (enemy.getName()) {
                    case "Cupcake":
                        canvas.drawBitmap(cupcake, enemy.getXPosition(), enemy.getYPosition(), null);
                        break;
                    case "IceSandwich":
                        canvas.drawBitmap(iceSandwich, enemy.getXPosition(), enemy.getYPosition(), null);
                        break;
                    case "KitKat":
                        canvas.drawBitmap(kitKat, enemy.getXPosition(), enemy.getYPosition(), null);
                        break;
                    case "Oreo":
                        canvas.drawBitmap(oreo, enemy.getXPosition(), enemy.getYPosition(), null);
                        break;
                }
            }
        }

        //draw the crosshair
        canvas.drawBitmap(crosshair, GameActivity.getCrosshairX(), GameActivity.getCrosshairY(), null);

        this.invalidate();
    }

    private void loadBitmaps() {
        crosshair = BitmapFactory.decodeResource(getResources(), R.drawable.red_crosshair);
        explosion = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
        cupcake = BitmapFactory.decodeResource(getResources(), R.drawable.android_cupcake);
        iceSandwich = BitmapFactory.decodeResource(getResources(), R.drawable.android_ice_cream_sandwich);
        kitKat = BitmapFactory.decodeResource(getResources(), R.drawable.android_kitkat);
        oreo = BitmapFactory.decodeResource(getResources(), R.drawable.android_oreo);
    }

    private void resizeBitmaps() {
        crosshair = getResizedImage(crosshair, pxFromDp(CROSSHAIR_SIZE_DP), pxFromDp(CROSSHAIR_SIZE_DP));
        explosion = getResizedImage(explosion, pxFromDp(44), pxFromDp(44));
        cupcake = getResizedImage(cupcake, pxFromDp(enemiesSize), pxFromDp(enemiesSize));
        iceSandwich = getResizedImage(iceSandwich, pxFromDp(enemiesSize), pxFromDp(enemiesSize));
        kitKat = getResizedImage(kitKat, pxFromDp(enemiesSize), pxFromDp(enemiesSize));
        oreo = getResizedImage(oreo, pxFromDp(enemiesSize), pxFromDp(enemiesSize));
    }

    public ArrayList<Enemy> getEnemies() {
        return this.enemyManager.getEnemiesList();
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        if (this.enemies != null)
            this.enemies.clear();
        this.enemies = enemies;
        gameResumed = true;
    }

    public void setPause() {
        gameResumed = false;
    }


    /*
    private void explode(final Canvas canvas, final float x, final float y) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        canvas.drawBitmap(explosion, x, y, null);
                    }
                });
            }
        }, 0, 200);
        //invalidate();
    }
    */

}
