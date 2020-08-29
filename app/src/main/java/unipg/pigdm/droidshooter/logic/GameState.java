package unipg.pigdm.droidshooter.logic;

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

    private String enemiesState;
    private int score;
    private long timeLeftInMillis;
    private float crosshairXPosition, crosshairYPosition;

    public GameState(ArrayList<Enemy> enemies, int score, long timeLeftInMillis, float crosshairXPosition, float crosshairYPosition) {
        this.enemiesState = toString(enemies);
        this.score = score;
        this.timeLeftInMillis = timeLeftInMillis;
        this.crosshairXPosition = crosshairXPosition;
        this.crosshairYPosition = crosshairYPosition;
    }

    public GameState(Parcel in) {
        enemiesState = in.readString();
        score = in.readInt();
        timeLeftInMillis = in.readLong();
        crosshairXPosition = in.readFloat();
        crosshairYPosition = in.readFloat();
    }

    public int getScore() {
        return score;
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

    private String toString(ArrayList<Enemy> enemies) {
        StringBuilder sb = new StringBuilder();
        for (Enemy enemy : enemies) {
            sb.append(enemy.toString());
        }
        return sb.toString();
    }

    public String getEnemies() {
        return this.enemiesState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(enemiesState);
        parcel.writeInt(score);
        parcel.writeLong(timeLeftInMillis);
        parcel.writeFloat(crosshairXPosition);
        parcel.writeFloat(crosshairYPosition);
    }
}
