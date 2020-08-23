package unipg.pigdm.droidshooter.model;

public class EnemyOreo extends Enemy {

    private String name = "Oreo";
    //private float killTime = 0.5f;
    //private float escapeTime = 5f;
    private int points = 4;

    public EnemyOreo() {

        this.setName(name);
        //this.setKillTime(killTime);
        //this.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.android_oreo));
        //this.setKillTime(killTime);
        this.setPoints(points);
    }
}
