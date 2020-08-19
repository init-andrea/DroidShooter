package unipg.pigdm.droidshooter.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import unipg.pigdm.droidshooter.R;
import unipg.pigdm.droidshooter.controller.GameActivity;
import unipg.pigdm.droidshooter.model.Enemy;
import unipg.pigdm.droidshooter.model.EnemyManager;

import static unipg.pigdm.droidshooter.util.Utilities.pxFromDp;

public class CustomGameView extends View {

    private static final int CROSSHAIR_SIZE_DP = 66;

    private Bitmap crosshair;
    private Bitmap explosion;
    private int score;
    private int aliveEnemies;
    private long timeLeft = 2000;
    private boolean gameEnded;
    private boolean gameWon;
    private Handler handler;

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

    public int getScore() {
        return score;
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

    private void init(@Nullable AttributeSet set) {
        //TODO

        density = getResources().getDisplayMetrics().density;
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
        enemyManager = new EnemyManager(this.getContext());
        aliveEnemies = enemyManager.getEnemiesList().size();
        handler = new Handler();
        crosshair = BitmapFactory.decodeResource(getResources(), R.drawable.red_crosshair);
        explosion = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);

        //Calculate image size at the start of the view
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);

                crosshair = enemyManager.getResizedImage(crosshair, pxFromDp(CROSSHAIR_SIZE_DP), pxFromDp(CROSSHAIR_SIZE_DP));
                explosion = enemyManager.getResizedImage(explosion, pxFromDp(44), pxFromDp(44));
            }
        });
    }

    @Override
    protected void onDraw(final Canvas canvas) {

        canvas.drawColor(Color.WHITE);

        //check if enemies are hit
        for (final Enemy enemy : enemyManager.getEnemiesList()) {
            if (enemyManager.isHit(enemy, GameActivity.getCrosshairX(), GameActivity.getCrosshairY(), CROSSHAIR_SIZE_DP, density) && !enemy.isDead()) {
                //explode(canvas, enemy.getXPosition(), enemy.getYPosition());
                /*
                CountDownTimer explosionAnimationTimer = new CountDownTimer(timeLeft, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeLeft = millisUntilFinished;
                        canvas.drawBitmap(explosion, enemy.getXPosition(), enemy.getYPosition(), null);
                    }

                    @Override
                    public void onFinish() {
                        //enemyManager.getEnemiesList().remove(enemy);
                    }
                }.start();
                */
                canvas.drawBitmap(explosion, enemy.getXPosition(), enemy.getYPosition(), null);
                enemy.setDead(true);
                aliveEnemies --;
                /*
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enemy.setDead(true);
                    }
                }, 1500);
                */

                GameActivity.updateScore(enemy.getPoints());
            }
        }

        //TODO add list of dead enemies to draw the image on screen with countdowntimer(?)

        if (aliveEnemies == 0) {
            GameActivity.winGame();
        }


        //loop through list of alive enemies to draw them
        for (Enemy enemy : enemyManager.getEnemiesList()) {
            if (enemy.isAlive()) {
                enemyManager.moveEnemy(enemy);
                canvas.drawBitmap(enemy.getImage(), enemy.getXPosition(), enemy.getYPosition(), null);
            }
        }

        //draw the crosshair
        canvas.drawBitmap(crosshair, GameActivity.getCrosshairX(), GameActivity.getCrosshairY(), null);

        this.invalidate();
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
