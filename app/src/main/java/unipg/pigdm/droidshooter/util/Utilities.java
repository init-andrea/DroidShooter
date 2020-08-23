package unipg.pigdm.droidshooter.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;

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
}
