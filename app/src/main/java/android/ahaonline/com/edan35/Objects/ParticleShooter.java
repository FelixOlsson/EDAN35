package android.ahaonline.com.edan35.Objects;

/**
 * Created by Felix on 2016-12-13.
 */



import android.opengl.Matrix;

import java.util.Random;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setRotateEulerM;
import static android.opengl.Matrix.setRotateM;


public class ParticleShooter extends transformController{
    private final float[] position;
    private final float[] direction;
    private final float[] color;

    private final float angle;
    private final float speed;
    private  float lifeTime;
    private  float age;

    private final Random random = new Random();

    private float[] rotationMatrix = new float[16];
    private float[] translationMatrix = new float[16];
    private float[] directionVec = new float[4];
    private float[] resultVec = new float[4];
    private float[] resultVec2 = new float[4];

    private boolean once = true;


    public ParticleShooter(float[] position, float[] direction, float[] color, float angle, float speed) {
        this.position = position;
        this.direction = direction;
        this.color = color;
        this.angle = angle;
        this.speed = speed;

        directionVec[0] = direction[0];
        directionVec[1] = direction[1];
        directionVec[2] = direction[2];

        //directionVec[0] = random.nextInt(1);
        //directionVec[1] = random.nextInt(1);
       // directionVec[2] = random.nextInt(1);
    }

    public void addParticles(ParticleSystem particleSystem, float currentTime, int count) {

        for (int i = 0; i < count; i++) {

            setRotateM(rotationMatrix, 0, (random.nextFloat() - 0.5f) * angle, 1, 0, 0);
            rotateM(rotationMatrix, 0, (random.nextFloat() - 0.5f) * angle, 0, 1, 0);
            rotateM(rotationMatrix, 0, (random.nextFloat() - 0.5f) * angle, 0, 0, 1);
            Matrix.setIdentityM(translationMatrix, 0);
            Matrix.translateM(translationMatrix, 0, random.nextFloat() * 10f, random.nextFloat()* 10f, random.nextFloat() * 10f);

            multiplyMV(resultVec, 0, rotationMatrix, 0, directionVec, 0);

            float randomSpeed = 1.0f + random.nextFloat() * speed;

            float[] newDirection = new float[]{resultVec[0] * randomSpeed, resultVec[1] * randomSpeed, resultVec[2] * randomSpeed};
            float[] newPosition = new float[]{translationMatrix[13], translationMatrix[14], translationMatrix[15]};

            particleSystem.addParticle(newPosition, color, newDirection, currentTime);
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