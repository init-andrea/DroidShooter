package unipg.pigdm.droidshooter.logic;

public class PlayerManager {

    private final float TIMEFRAME = 0.006f;

    private float xPosition, xVelocity;
    private float yPosition, yVelocity;
    private float xMax, yMax;

    public PlayerManager() {
        this.xPosition = this.xVelocity = 0.0f;
        this.yPosition = this.yVelocity = 0.0f;
    }

    public float getTIMEFRAME() {
        return TIMEFRAME;
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

    public float getXVelocity() {
        return xVelocity;
    }

    public void setXVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    public float getYVelocity() {
        return yVelocity;
    }

    public void setYVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    public float getXMax() {
        return xMax;
    }

    public void setXMax(float xMax) {
        this.xMax = xMax;
    }

    public float getYMax() {
        return yMax;
    }

    public void setYMax(float yMax) {
        this.yMax = yMax;
    }

    public void calculatePosition(float xAcceleration, float yAcceleration) {

        //Calculate new speed
        xVelocity += (xAcceleration * TIMEFRAME);
        yVelocity += (yAcceleration * TIMEFRAME);

        //Calculate distance travelled in the time frame
        float xS = xVelocity + (xAcceleration - 9.81f / 2.0f) * TIMEFRAME * TIMEFRAME;
        float yS = yVelocity + (yAcceleration - 9.81f / 2.0f) * TIMEFRAME * TIMEFRAME;

        //Add to position negative for x value because sensor reads the opposite of what we want
        xPosition -= xS;
        yPosition += yS;

        checkVelocityAndMaxXY();

    }

    //Check if X or Y coordinates exceed the screen size and if the crosshair is on a side resets the velocity
    private void checkVelocityAndMaxXY() {
        if (xPosition > xMax) {
            xPosition = xMax;
            xVelocity = 0;
        } else if (xPosition < 0) {
            xPosition = 0;
            xVelocity = 0;
        }
        if (yPosition > yMax) {
            yPosition = yMax;
            yVelocity = 0;
        } else if (yPosition < 0) {
            yPosition = 0;
            yVelocity = 0;
        }
    }

    public void resetCrosshair() {
        xPosition = xVelocity = yPosition = yVelocity = 0.0f;
    }
}
