package android.ahaonline.com.edan35.Objects;

import android.ahaonline.com.edan35.data.VertexArray;
import android.ahaonline.com.edan35.programs.ParticleShaderProgram;
import android.graphics.Color;


import static android.opengl.GLES30.*;

import static android.ahaonline.com.edan35.util.Constants.BYTES_PER_FLOAT;

/**
 * Created by Felix on 2016-12-13.
 */

public class ParticleSystem  extends transformController{
    private ParticleShaderProgram particleShaderProgram;

    private final float[] vert;
    private final float[] col;
    private final float[] vect;
    private final float[] time;

    private final VertexArray posArray;
    private final VertexArray colorArray;
    private final VertexArray vectArray;
    private final VertexArray timeArray;

    private final int maxParticleCount;

    private int currentParticleCount;
    private int nextParticle;

    public ParticleSystem(int maxParticleCount) {
        super();
        vert = new float[maxParticleCount * 3];
        col = new float[maxParticleCount * 3];
        vect = new float[maxParticleCount * 3];
        time = new float[maxParticleCount * 1];
        posArray = new VertexArray(vert);
        colorArray = new VertexArray(col);
        vectArray = new VertexArray(vect);
        timeArray = new VertexArray(time);
        this.maxParticleCount = maxParticleCount;
    }

    public void addParticle(float[] position, int color, float[] direction, float particleStartTime) {

        final int posNumber = nextParticle * 3;
        final int colNumber = nextParticle * 3;
        final int vecNumber = nextParticle * 3;
        final int timeNumber = nextParticle * 1;
        int posOffset = nextParticle * 3;
        int colOffset = nextParticle * 3;
        int vecOffset = nextParticle * 3;
        int timeOffset = nextParticle * 1;
        nextParticle++;

        if (currentParticleCount < maxParticleCount) {
            currentParticleCount++;
        }

        if (nextParticle == maxParticleCount) {
            nextParticle = 0;
        }

        vert[posOffset++] = position[0];
        vert[posOffset++] = position[1];
        vert[posOffset++] = position[2];

        col[colOffset++] = Color.red(color) / 255f;
        col[colOffset++] = Color.green(color) / 255f;
        col[colOffset++] = Color.blue(color) / 255f;

        vect[vecOffset++] = direction[0];
        vect[vecOffset++] = direction[1];
        vect[vecOffset++] = direction[2];

        time[timeOffset++] = particleStartTime;

        posArray.updateBuffer(vert, posNumber, 3);
        colorArray.updateBuffer(col, colNumber, 3);
        vectArray.updateBuffer(vect, vecNumber, 3);
        timeArray.updateBuffer(time, timeNumber, 1);
    }

    public void bindData(ParticleShaderProgram particleProgram) {


        this.particleShaderProgram = particleProgram;
        posArray.setVertexAttribPointer(0,
                particleProgram.getPositionAttributeLocation(),
                3, 0);

        colorArray.setVertexAttribPointer(0,
                particleProgram.getColorAttributeLocation(),
                3, 0);

        vectArray.setVertexAttribPointer(0,
                particleProgram.getDirectionVectorAttributeLocation(),
                3, 0);


        timeArray.setVertexAttribPointer(0,
                particleProgram.getParticleStartTimeAttributeLocation(),
                1, 0);
    }

    public void draw() {
        glDrawArrays(GL_POINTS, 0, currentParticleCount);
        glDisableVertexAttribArray(particleShaderProgram.getPositionAttributeLocation());
        glDisableVertexAttribArray(particleShaderProgram.getColorAttributeLocation());
        glDisableVertexAttribArray(particleShaderProgram.getDirectionVectorAttributeLocation());
        glDisableVertexAttribArray(particleShaderProgram.getParticleStartTimeAttributeLocation());
    }
}
