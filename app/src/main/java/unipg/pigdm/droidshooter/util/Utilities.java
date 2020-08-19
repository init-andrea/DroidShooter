package unipg.pigdm.droidshooter.util;

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
}
