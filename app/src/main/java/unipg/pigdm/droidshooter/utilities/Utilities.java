package unipg.pigdm.droidshooter.utilities;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Arrays;

import unipg.pigdm.droidshooter.logic.Enemy;
import unipg.pigdm.droidshooter.logic.EnemyCupcake;
import unipg.pigdm.droidshooter.logic.EnemyIceSandwich;
import unipg.pigdm.droidshooter.logic.EnemyKitKat;
import unipg.pigdm.droidshooter.logic.EnemyOreo;
import unipg.pigdm.droidshooter.view.CustomGameView;

public class Utilities {

    public static int checkUserInput(String input, int defaultVal) {
        if (input.isEmpty())
            return defaultVal;
        return Integer.parseInt(input);
    }

    public static float checkUserInput(String input, float defaultVal) {
        if (input.isEmpty())
            return defaultVal;
        return Float.parseFloat(input);
    }

    public static long checkUserInput(String input, long defaultVal) {
        if (input.isEmpty())
            return defaultVal;
        return Long.parseLong(input);
    }

    //From dp to px
    public static int pxFromDp(float dp) {
        return (int) (dp * CustomGameView.getDensity());
    }

    //From px to dp
    public static int dpFromPx(float px) {
        return (int) (px / CustomGameView.getDensity());
    }

    //Resize bitmap image
    public static Bitmap getResizedImage(Bitmap bitmap, int reqWidth, int reqHeight) {

        Matrix matrix = new Matrix();
        RectF src = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF dst = new RectF(0, 0, reqWidth, reqHeight);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static ArrayList<Enemy> arrayListFromString(String enemies) {
        String[] tokens = enemies.split("name:");
        tokens = Arrays.copyOfRange(tokens, 1, tokens.length);
        ArrayList<Enemy> enemiesList = new ArrayList<>();
        for (String token : tokens) {
            int[] movementInfos = new int[2];
            String[] singleEnemy = token.split(" ");
            switch (singleEnemy[0]) {
                case "Cupcake":
                    enemiesList.add(new EnemyCupcake());
                    break;
                case "IceSandwich":
                    enemiesList.add(new EnemyIceSandwich());
                    break;
                case "KitKat":
                    enemiesList.add(new EnemyKitKat());
                    break;
                case "Oreo":
                    enemiesList.add(new EnemyOreo());
                    break;
            }
            enemiesList.get(enemiesList.size() - 1).setXPosition(Float.parseFloat(singleEnemy[1].substring(2)));
            enemiesList.get(enemiesList.size() - 1).setYPosition(Float.parseFloat(singleEnemy[2].substring(2)));
            enemiesList.get(enemiesList.size() - 1).setAlive(singleEnemy[3].substring(2).equals("t"));
            enemiesList.get(enemiesList.size() - 1).setDead(singleEnemy[4].substring(2).equals("t"));
            movementInfos[0] = Integer.parseInt(singleEnemy[5].substring(2));
            movementInfos[1] = Integer.parseInt(singleEnemy[6].substring(2));
            enemiesList.get(enemiesList.size() - 1).setMovementInfo(movementInfos);
            enemiesList.get(enemiesList.size() - 1).setMoving(Boolean.parseBoolean(singleEnemy[7].substring(2)));
        }
        return enemiesList;
    }
}
