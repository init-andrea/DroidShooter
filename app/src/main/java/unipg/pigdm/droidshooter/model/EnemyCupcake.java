package unipg.pigdm.droidshooter.model;

import android.content.Context;
import android.graphics.BitmapFactory;

import unipg.pigdm.droidshooter.R;

public class EnemyCupcake extends Enemy {

    private String name = "Cupcake";
    private float killTime = 0.5f;
    private float escapeTime = 5f;
    private int points = 1;

    EnemyCupcake(Context context) {

        this.setName(name);
        this.setKillTime(killTime);
        this.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.android_cupcake));
        this.setEscapeTime(escapeTime);
        this.setPoints(points);
    }

}
