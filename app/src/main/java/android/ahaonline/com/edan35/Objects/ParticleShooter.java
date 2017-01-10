package android.ahaonline.com.edan35.Objects;

/**
 * Created by Felix on 2016-12-13.
 */



import java.util.Random;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.setRotateEulerM;



public class ParticleShooter extends transformController{
    private final float[] position;
    private final float[] direction;
    private final float[] color;

    private final float angleVariance;
    private final float speedVariance;

    private final Random random = new Random();

    private float[] rotationMatrix = new float[16];
    private float[] directionVec = new float[4];
    private float[] resultVec = new float[4];

    private boolean once = true;


    public ParticleShooter(float[] position, float[] direction, float[] color, float angleVarianceInDegrees, float speedVariance) {
        this.position = position;
        this.direction = direction;
        this.color = color;
        this.angleVariance = angleVarianceInDegrees;
        this.speedVariance = speedVariance;

        directionVec[0] = direction[0];
        directionVec[1] = direction[1];
        directionVec[2] = direction[2];

        //directionVec[0] = random.nextInt(1);
        //directionVec[1] = random.nextInt(1);
       // directionVec[2] = random.nextInt(1);
    }

    public void addParticles(ParticleSystem particleSystem, float currentTime, int count) {

        for (int i = 0; i < count; i++) {

            setRotateEulerM(rotationMatrix, 0, (random.nextFloat() - 0.5f) * angleVariance,
                    (random.nextFloat() - 0.5f) * angleVariance, (random.nextFloat() - 0.5f) * angleVariance);


            multiplyMV(resultVec, 0, rotationMatrix, 0, directionVec, 0);

            float speed = 1.0f + random.nextFloat() * speedVariance;

            float[] newDirection = new float[]{resultVec[0] * speed, resultVec[1] * speed, resultVec[2] * speed};

            particleSystem.addParticle(position, color, newDirection, currentTime);
        }
    }

    public boolean once() {
        if(once) {
            once = false;
            return true;
        }

        return false;
    }
}