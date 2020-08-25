package unipg.pigdm.droidshooter.logic;

public class EnemyCupcake extends Enemy {

    private String name = "Cupcake";
    //private float killTime = 0.5f;
    //private float escapeTime = 5f;
    private int points = 1;

    EnemyCupcake() {

        this.setName(name);
        //this.setKillTime(killTime);
        //this.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.android_cupcake));
        //this.setKillTime(killTime);
        this.setPoints(points);
    }

}
