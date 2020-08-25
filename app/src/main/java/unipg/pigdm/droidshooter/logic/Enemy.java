package unipg.pigdm.droidshooter.logic;

import android.os.Parcel;
import android.os.Parcelable;

public class Enemy implements Parcelable {

    private String name;
    public static final Creator<Enemy> CREATOR = new Creator<Enemy>() {
        @Override
        public Enemy createFromParcel(Parcel in) {
            return new Enemy(in);
        }

        @Override
        public Enemy[] newArray(int size) {
            return new Enemy[size];
        }
    };
    private float yPosition;
    //private Bitmap image;
    private float xPosition;
    private int enemySizeDp = 44;

    //[screen side (0 top, 1 right, 2 bottom, 3 left), px on the side]
    private int[] movementInfo = new int[2];

    private boolean alive = false;
    private boolean dead = false;
    private boolean moving = false;

    protected Enemy() {
        //Constructor, does nothing
    }

    //private float killTime;
    //private float escapeTime;
    private int points;

    protected Enemy(Parcel in) {
        name = in.readString();
        xPosition = in.readFloat();
        yPosition = in.readFloat();
        enemySizeDp = in.readInt();
        points = in.readInt();
        movementInfo = in.createIntArray();
        alive = in.readByte() != 0;
        dead = in.readByte() != 0;
        moving = in.readByte() != 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

     */

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

    /*
    public float getKillTime() {
        return killTime;
    }

    public void setKillTime(float killTime) {
        this.killTime = killTime;
    }

    public float getEscapeTime() {
        return escapeTime;
    }

    public void setEscapeTime(float escapeTime) {
        this.escapeTime = escapeTime;
    }
    */

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeFloat(xPosition);
        parcel.writeFloat(yPosition);
        parcel.writeInt(enemySizeDp);
        parcel.writeInt(points);
        parcel.writeIntArray(movementInfo);
        parcel.writeByte((byte) (alive ? 1 : 0));
        parcel.writeByte((byte) (dead ? 1 : 0));
        parcel.writeByte((byte) (moving ? 1 : 0));
    }

    //Converts the enemy state into a readable string. "t" stands for true, "f" for false, "s" for side of the screen and "p" for pixels
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" name:").append(this.name);
        sb.append(" x:").append(this.xPosition);
        sb.append(" y:").append(this.yPosition);
        sb.append(" a:");
        if (this.isAlive())
            sb.append("t");
        else
            sb.append("f");
        sb.append(" d:");
        if (this.isDead())
            sb.append("t");
        else
            sb.append("f");
        sb.append(" s:").append(this.getMovementInfo()[0]);
        sb.append(" p:").append(this.getMovementInfo()[1]);
        sb.append(" m:").append(this.isMoving());
        return sb.toString();
    }
}
