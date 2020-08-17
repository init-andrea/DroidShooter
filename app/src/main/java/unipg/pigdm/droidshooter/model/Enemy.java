package unipg.pigdm.droidshooter.model;

import android.graphics.Bitmap;

public class Enemy {

    private String name;
    private Bitmap image;
    private float xPosition;
    private float yPosition;
    private float killTime;
    private float escapeTime;
    private int points;

    //[screen side (0 top, 1 right, 2 bottom, 3 left), px on the side]
    private int[] movementInfo = new int[2];


    private int enemySizeDp = 44;
    private boolean alive = false;
    private boolean dead = false;
    private boolean moving = false;

    protected Enemy() {
        //Constructor, does nothing
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public float getXPosition() {
        return xPosition;
    }

    public void setXPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public float getYPosition() {
        return yPosition;
    }

    public void setYPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public float getKillTime() {
        return killTime;
    }

    public void setKillTime(float killTime) {
        this.killTime = killTime;
    }

    public float getEscapeTime() {
        return escapeTime;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public int[] getMovementInfo() {
        return movementInfo;
    }

    public void setMovementInfo(int[] movementInfo) {
        this.movementInfo = movementInfo;
    }

    public int getSize() {
        return enemySizeDp;
    }

    public void setEscapeTime(float escapeTime) {
        this.escapeTime = escapeTime;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}
