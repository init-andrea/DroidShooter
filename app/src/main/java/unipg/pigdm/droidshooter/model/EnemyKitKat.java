package unipg.pigdm.droidshooter.model;

public class EnemyKitKat extends Enemy {

    private String name = "KitKat";
    //private float killTime = 0.5f;
    //private float escapeTime = 5f;
    private int points = 3;

    EnemyKitKat() {

        this.setName(name);
        //this.setKillTime(killTime);
        //this.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.android_kitkat));
        //this.setKillTime(killTime);
        this.setPoints(points);
    }

}
