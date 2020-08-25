package unipg.pigdm.droidshooter.logic;

public class PlayerMovement {

    private float xPosition, xAcceleration, xVelocity = 0.0f;
    private float yPosition, yAcceleration, yVelocity = 0.0f;
    private float frameTime = 0.002f;

    public float[] calculateAccel(float xPosition, float yPosition, float xAcceleration, float yAcceleration, float xMax, float yMax) {

        float[] values = new float[4];

        //Calculate new speed
        xVelocity += (xAcceleration * frameTime);
        yVelocity += (yAcceleration * frameTime);

        //Calculate distance travelled in the time frame
        float xS = xVelocity + (xAcceleration - 9.81f / 2.0f) * frameTime * frameTime;
        float yS = yVelocity + (yAcceleration - 9.81f / 2.0f) * frameTime * frameTime;

        //Add to position negative for x value because sensor reads the opposite of what we want
        xPosition -= xS;
        yPosition += yS;

        if (xPosition > xMax) {
            xPosition = xMax;
        } else if (xPosition < 0) {
            xPosition = 0;
        }
        if (yPosition > yMax) {
            yPosition = yMax;
        } else if (yPosition < 0) {
            yPosition = 0;
        }

        return values;
    }

}
