package unipg.pigdm.droidshooter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

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

    public GameState(String enemiesState, int score, long timeLeftInMillis, float crosshairXPosition, float crosshairYPosition) {
        this.enemiesState = enemiesState;
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

    public static ArrayList<Enemy> arrayListFromString(String enemies) {
        String[] tokens = enemies.split("name:");
        tokens = Arrays.copyOfRange(tokens, 1, tokens.length);
        ArrayList<Enemy> enemiesList = new ArrayList<>();
        //Log.d("token0", tokens[0]);
        for (String token : tokens) {
            int[] movementInfos = new int[2];
            String[] singleEnemy = token.split(" ");
            switch (singleEnemy[0]) {
                case "Cupcake":
                    enemiesList.add(new EnemyCupcake());
                    break;
                case "IceSandwich":
                    enemiesList.add(new EnemyIceSandwich());
                    break;
                case "KitKat":
                    enemiesList.add(new EnemyKitKat());
                    break;
                case "Oreo":
                    enemiesList.add(new EnemyOreo());
                    break;
            }
            enemiesList.get(enemiesList.size() - 1).setXPosition(Float.parseFloat(singleEnemy[1].substring(2)));
            enemiesList.get(enemiesList.size() - 1).setYPosition(Float.parseFloat(singleEnemy[2].substring(2)));
            enemiesList.get(enemiesList.size() - 1).setAlive(singleEnemy[3].substring(2).equals("t"));
            enemiesList.get(enemiesList.size() - 1).setDead(singleEnemy[4].substring(2).equals("t"));
            movementInfos[0] = Integer.parseInt(singleEnemy[5].substring(2));
            movementInfos[1] = Integer.parseInt(singleEnemy[6].substring(2));
            enemiesList.get(enemiesList.size() - 1).setMovementInfo(movementInfos);
            enemiesList.get(enemiesList.size() - 1).setMoving(Boolean.parseBoolean(singleEnemy[7].substring(2)));

        }
        /*
        Log.d("tokens", Arrays.toString(tokens));
        Log.d("tokensSize", String.valueOf(tokens.length));
        Log.d("newToken", Arrays.toString(Arrays.copyOfRange(tokens, 1, tokens.length)));
        Log.d("token1SplitX", tokens[1].split(" ")[2].substring(2));
        */
        return enemiesList;
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

    public static String toString(ArrayList<Enemy> enemies) {
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
