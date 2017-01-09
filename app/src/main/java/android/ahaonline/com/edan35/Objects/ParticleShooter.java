package android.ahaonline.com.edan35.Objects;

/**
 * Created by Felix on 2016-12-13.
 */



import android.opengl.Matrix;

import java.util.Random;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.setRotateEulerM;



public class ParticleShooter extends transformController{
    private final float[] position;
    private final float[] direction;
    private final int color;

    private final float angleVariance;
    private final float speedVariance;

    private final Random random = new Random();

    private float[] rotationMatrix = new float[16];
    private float[] directionVector = new float[4];
    private float[] resultVector = new float[4];

    private boolean once = true;


    public ParticleShooter(float[] position, float[] direction, int color, float angleVarianceInDegrees, float speedVariance) {
        this.position = position;
        this.direction = direction;
        this.color = color;
        this.angleVariance = angleVarianceInDegrees;
        this.speedVariance = speedVariance;

        directionVector[0] = direction[0];
        directionVector[1] = direction[1];
        directionVector[2] = direction[2];

        //directionVector[0] = random.nextInt(1);
        //directionVector[1] = random.nextInt(1);
       // directionVector[2] = random.nextInt(1);
    }

    public void addParticles(ParticleSystem particleSystem, float currentTime, int count) {

        for (int i = 0; i < count; i++) {

            setRotateEulerM(rotationMatrix, 0, (random.nextFloat() - 0.5f) * angleVariance,
                    (random.nextFloat() - 0.5f) * angleVariance, (random.nextFloat() - 0.5f) * angleVariance);


            multiplyMV(resultVector, 0, rotationMatrix, 0, directionVector, 0);

            float speedAdjustment = 1f + random.nextFloat() * speedVariance;

            float[] thisDirection = new float[]{
                    resultVector[0] * speedAdjustment,
                    resultVector[1] * speedAdjustment,
                    resultVector[2] * speedAdjustment};


            particleSystem.addParticle(position, color, thisDirection, currentTime);
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