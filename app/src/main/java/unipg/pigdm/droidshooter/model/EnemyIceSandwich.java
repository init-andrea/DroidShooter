package unipg.pigdm.droidshooter.model;

import android.content.Context;
import android.graphics.BitmapFactory;

import unipg.pigdm.droidshooter.R;

public class EnemyIceSandwich extends Enemy {

    private String name = "IceSandwich";
    private float killTime = 0.5f;
    private float escapeTime = 5f;
    private int points = 2;

    EnemyIceSandwich(Context context) {
        this.setName(name);
        this.setKillTime(killTime);
        this.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.android_ice_cream_sandwich));
        this.setEscapeTime(escapeTime);
        this.setPoints(points);
    }
}
