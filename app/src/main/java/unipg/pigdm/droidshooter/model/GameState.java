package unipg.pigdm.droidshooter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class GameState implements Parcelable {

    public static final Creator<GameState> CREATOR = new Creator<GameState>() {
        @Override
        public GameState createFromParcel(Parcel in) {
            return new GameState(in);
        }

        @Override
        public GameState[] newArray(int size) {
            return new GameState[size];
        }
    };
    private ArrayList enemies;
    private int score;
    private long timeLeftInMillis;
    private float crosshairXPosition, crosshairYPosition;

    public GameState(ArrayList<Enemy> enemiesList, int score, long timeLeftInMillis, float crosshairXPosition, float crosshairYPosition) {
        if (this.enemies != null)
            this.enemies.clear();
        this.enemies = enemiesList;
        this.score = score;
        this.timeLeftInMillis = timeLeftInMillis;
        this.crosshairXPosition = crosshairXPosition;
        this.crosshairYPosition = crosshairYPosition;
    }

    public GameState(Parcel in) {
        score = in.readInt();
        timeLeftInMillis = in.readLong();
        crosshairXPosition = in.readFloat();
        crosshairYPosition = in.readFloat();
        enemies = in.readArrayList(null);
    }

    public ArrayList getEnemies() {
        return this.enemies;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getTimeLeftInMillis() {
        return timeLeftInMillis;
    }

    public float getCrosshairXPosition() {
        return crosshairXPosition;
    }

    public float getCrosshairYPosition() {
        return crosshairYPosition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //parcel.writeList(enemies);
        parcel.writeInt(score);
        parcel.writeLong(timeLeftInMillis);
        parcel.writeFloat(crosshairXPosition);
        parcel.writeFloat(crosshairYPosition);
    }
}
