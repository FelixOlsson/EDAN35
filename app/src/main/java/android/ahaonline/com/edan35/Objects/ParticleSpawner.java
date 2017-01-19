package android.ahaonline.com.edan35.Objects;

/**
 * Created by Felix on 2016-12-13.
 */



import android.opengl.Matrix;
import static android.ahaonline.com.edan35.util.Geometry.Vec3;

import java.util.Random;

import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setRotateM;


public class ParticleSpawner extends transformController{
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


    public ParticleSpawner(float[] position, float[] direction, float[] color, float angle, float speed) {
        this.position = position;
        this.direction = direction;
        this.color = color;
        this.angle = angle;
        this.speed = speed;

        directionVec[0] = direction[0];
        directionVec[1] = direction[1];
        directionVec[2] = direction[2];

        //directionVec[0] = random.nextInt();
        //directionVec[1] = random.nextInt();
       // directionVec[2] = random.nextInt();
    }

    public void spawnParticles(ParticleSystem particleSystem, float currentTime, int count) {

        for (int i = 0; i < count; i++) {

            setRotateM(rotationMatrix, 0, (random.nextFloat() - 0.5f) * angle, 1, 0, 0);
            rotateM(rotationMatrix, 0, (random.nextFloat() - 0.5f) * angle, 0, 1, 0);
            rotateM(rotationMatrix, 0, (random.nextFloat() - 0.5f) * angle, 0, 0, 1);

            float polarAngle = (float)Math.toRadians((double)randomNumber(0f, 180f));
            float azimuth = (float)Math.toRadians((double) randomNumber(0f, 360f));

            float radius = (float)Math.toRadians((double) randomNumber(0f, 25f));

            float X = (float)Math.sin(polarAngle) * (float)Math.cos(azimuth);
            float Y = (float)Math.sin(polarAngle) * (float)Math.sin(azimuth);
            float Z = (float)Math.cos(polarAngle);

            Vec3 vector = new Vec3(X, Y, Z);

            Vec3 SpherePosition = vector.scale(radius).translate(new Vec3(position[0], position[1], position[2]));


            Matrix.setIdentityM(translationMatrix, 0);
            Matrix.translateM(translationMatrix, 0, random.nextFloat() * 10f, random.nextFloat()* 10f, random.nextFloat() * 10f);

            multiplyMV(resultVec, 0, rotationMatrix, 0, directionVec, 0);

            float randomSpeed = 1.0f + random.nextFloat() * speed;

            float[] newDirection = new float[]{resultVec[0] * randomSpeed, resultVec[1] * randomSpeed, resultVec[2] * randomSpeed};
            float[] newPosition = new float[]{translationMatrix[13], translationMatrix[14], translationMatrix[15]};

            particleSystem.spawnParticle(new float[] {SpherePosition.x, SpherePosition.y, SpherePosition.z}, color, newDirection, currentTime);
            // comment
        }
    }

    public boolean once() {
        if(once) {
            once = false;
            return true;
        }

        return false;
    }

    private float randomNumber(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }
}