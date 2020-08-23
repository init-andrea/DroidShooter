package unipg.pigdm.droidshooter.model;

public class EnemyIceSandwich extends Enemy {

    private String name = "IceSandwich";
    //private float killTime = 0.5f;
    //private float escapeTime = 5f;
    private int points = 2;

    EnemyIceSandwich() {

        this.setName(name);
        //this.setKillTime(killTime);
        //this.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.android_ice_cream_sandwich));
        //this.setKillTime(killTime);
        this.setPoints(points);
    }

}
