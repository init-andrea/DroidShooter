package unipg.pigdm.droidshooter.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Random;

import unipg.pigdm.droidshooter.controller.GameActivity;
import unipg.pigdm.droidshooter.controller.SettingsActivity;
import unipg.pigdm.droidshooter.controller.StartGameActivity;
import unipg.pigdm.droidshooter.view.CustomGameView;

public class EnemyManager {

    private Random generator;
    private int enemyNumber;
    private float enemySpeed;
    private float density, xMax, yMax;

    private ArrayList<Enemy> enemiesList;

    public EnemyManager(Context context) {
        enemiesList = new ArrayList<>();
        xMax = CustomGameView.getMaxWidth() - pxFromDp(44);
        yMax = CustomGameView.getMaxHeight() - pxFromDp(44);
        density = CustomGameView.getDensity();
        enemyNumber = GameActivity.getEnemyNumber();
        enemySpeed = GameActivity.getEnemySpeed();
        spawnEnemyList(enemiesList, context);
        resizeEnemies(enemiesList, density);
        for (Enemy enemy : enemiesList) {
            enemy.setAlive(true);
            this.setRandomSpawnEnemyX(enemy);
            this.setRandomSpawnEnemyY(enemy);
        }
    }

    public ArrayList<Enemy> getEnemiesList() {
        return enemiesList;
    }

    public void moveEnemy(Enemy enemy) {
        if (enemy.isMoving()) {
            int enemyMovementInfoPx = enemy.getMovementInfo()[1];
            switch (enemy.getMovementInfo()[0]) {
                case 0:     //top case
                    calculateMovementAndSetNewCoordinates(enemy, enemyMovementInfoPx, 0);
                    break;
                case 1:     //right case
                    calculateMovementAndSetNewCoordinates(enemy, xMax, enemyMovementInfoPx);
                    break;
                case 2:     //bottom case
                    calculateMovementAndSetNewCoordinates(enemy, enemyMovementInfoPx, yMax);
                    break;
                case 3:     //left case
                    calculateMovementAndSetNewCoordinates(enemy, 0, enemyMovementInfoPx);
                }
            if (enemy.getXPosition() >= xMax) {
                enemy.setXPosition(xMax);
                enemy.setMoving(false);
            } else if (enemy.getXPosition() <= 0) {
                enemy.setXPosition(0);
                enemy.setMoving(false);
            }
            if (enemy.getYPosition() >= yMax) {
                enemy.setYPosition(yMax);
                enemy.setMoving(false);
            } else if (enemy.getYPosition() <= 0) {
                enemy.setYPosition(0);
                enemy.setMoving(false);
            }
        }
        if (!enemy.isMoving()){
            chooseScreenSide(enemy);
        }

    }

    private void chooseScreenSide(Enemy enemy) {
        int[] info = new int[2];
        info[0] = generator.nextInt(4);
        if (info[0] == 0 || info[0] == 2) {
            info[1] = generator.nextInt((int)xMax);
        } else if (info[0] == 1 || info[0] == 3) {
            info[1] = generator.nextInt((int)yMax);
        }
        enemy.setMovementInfo(info);
        enemy.setMoving(true);
    }

    private void calculateMovementAndSetNewCoordinates(Enemy enemy, float targetX, float targetY) {
        float currentX = enemy.getXPosition();
        float currentY = enemy.getYPosition();
        float deltaX = targetX - currentX;
        float deltaY = targetY - currentY;
        float angle = (float) Math.atan2(deltaY, deltaX);
        currentX += enemySpeed * Math.cos(angle);
        currentY += enemySpeed * Math.sin(angle);
        enemy.setXPosition(currentX);
        enemy.setYPosition(currentY);
    }

    private Enemy chooseEnemyType(int i, Context context) {
        Enemy enemy = null;

        if (i == 0) {
            enemy = new EnemyCupcake(context);
        } else if (i == 1) {
            enemy = new EnemyIceSandwich(context);
        } else if (i == 2) {
            enemy = new EnemyKitKat(context);
        } else if (i == 3) {
            enemy = new EnemyOreo(context);
        }
        return enemy;
    }

    public void setRandomSpawnEnemyX(Enemy enemy) {
        enemy.setXPosition(generator.nextInt((int)xMax));
    }

    public void setRandomSpawnEnemyY(Enemy enemy) {
        enemy.setYPosition(generator.nextInt((int)yMax));
    }

    public boolean isHit(Enemy e, float x, float y, int CROSSHAIR_SIZE_DP, float density) {
            if (   (e.getXPosition()+pxFromDp(e.getSize())) < (x+pxFromDp(CROSSHAIR_SIZE_DP))
                    && (e.getXPosition()) > (x)
                    && (e.getYPosition()) > (y)
                    && (e.getYPosition()+pxFromDp(e.getSize())) < (y+pxFromDp(CROSSHAIR_SIZE_DP))
            ) {
                e.setAlive(false);
                return true;
            }
            else {
                return false;
            }

    }

    public void destroyDeadEnemies(ArrayList<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (enemy.isDead()) {
                enemies.remove(enemy);
            }
        }
    }

    private void spawnEnemyList(ArrayList<Enemy> arrayList, Context context) {
        generator = new Random();
        for (int i = 0; i < enemyNumber; i++) {
            arrayList.add(chooseEnemyType(generator.nextInt(4), context));
        }
    }

    private void resizeEnemies(ArrayList<Enemy> arrayList, float density) {
        for (Enemy enemy : arrayList) {
            enemy.setImage(getResizedImage(enemy.getImage(), pxFromDp(enemy.getSize()), pxFromDp(enemy.getSize())));
        }
    }

    //Resize bitmap image
    public Bitmap getResizedImage(Bitmap bitmap, int reqWidth, int reqHeight) {

        Matrix matrix = new Matrix();
        RectF src = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF dst = new RectF(0, 0, reqWidth, reqHeight);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //From dp to px
    public static int pxFromDp(float dp) {
        return (int) (dp * CustomGameView.getDensity());
    }

    //from px to dp
    public static int dpFromPx(float px) {
        return (int) (px / CustomGameView.getDensity());
    }
}
