package android.ahaonline.com.edan35.util;

import android.opengl.Matrix;

import static android.R.attr.rotationX;
import static android.R.attr.rotationY;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static java.lang.Math.*;

/**
 * Created by felix on 09/12/2016.
 */
public class Camera {

    private float x, y, z;
    private float rotationX, rotationY, rotationZ;
    private float vel, rot;

    //temporary values
    private float tX = 0, tY = 0, tZ = 0;
    private float tRotationX = 0, tRotationY = 0, tRotationZ = 0;
    private float tSizeX = 1, tSizeY = 1, tSizeZ = 1;


    private static final float[] viewMatrix = new float[16];

    public Camera() {
        setIdentityM(viewMatrix, 0);
    }


    public void translate(float x, float y, float z) {
        this.x += x;
        tX = x;
        this.y += y;
        tY = y;
        this.z += z;
        tZ = z;
    }

    public void rotateX(float degree) {
        rotationX += degree;
        tRotationX = degree;
    }

    public void rotateY(float degree) {
        rotationY += degree;
        tRotationY = degree;
    }

    public void rotateZ(float degree) {
        rotationZ += degree;
        tRotationZ = degree;
    }

    public void scale(float size) {
        tSizeX = size;
        tSizeY = size;
        tSizeZ = size;
    }

    public void scaleX(float size) {
        tSizeX += size;
    }

    public void scaleY(float size) {
        tSizeY += size;
    }

    public void scaleZ(float size) {
        tSizeZ += size;
    }

    public float getX() {
        return viewMatrix[12];
    }

    public float getY() {
        return  viewMatrix[13];
    }

    public float getZ() {
        return  viewMatrix[14];
    }

    public float[] getViewMatrix() {
        return viewMatrix;
    }
    public void transformMatrix() {
        scaleM(viewMatrix, 0, tSizeX, tSizeY, tSizeZ);
        rotateM(viewMatrix, 0, tRotationX, 1f, 0f, 0f);
        rotateM(viewMatrix, 0, tRotationY, 0f, 1f, 0f);
        rotateM(viewMatrix, 0, tRotationZ, 0f, 0f, 1f);
        translateM(viewMatrix, 0, tX, tY, tZ);

        resetValues();
    }

    private void resetValues() {
        tSizeX = 1;
        tSizeY = 1;
        tSizeZ = 1;

        tRotationX = 0;
        tRotationY = 0;
        tRotationZ = 0;

        tX = 0;
        tY = 0;
        tZ = 0;
    }



}
