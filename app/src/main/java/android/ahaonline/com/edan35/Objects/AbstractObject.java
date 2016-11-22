package android.ahaonline.com.edan35.Objects;

import android.opengl.Matrix;

import static android.R.attr.rotation;
import static android.R.attr.x;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;

/**
 * Created by Felix on 2016-11-21.
 */

public class AbstractObject {
    protected float x, y, z;
    protected float rotationX, rotationY, rotationZ;
    protected final float[] modelMatrix = new float[16];


    public void translate(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        Matrix.translateM(modelMatrix, 0, x, y, z);
    }

    public void rotateX(float degree) {
        rotateM(modelMatrix, 0, degree, 1f, 0f, 0f);
    }

    public void rotateY(float degree) {
        rotateM(modelMatrix, 0, degree, 0f, 1f, 0f);
    }

    public void rotateZ(float degree) {
        rotateM(modelMatrix, 0, degree, 0f, 0f, 1f);
    }

    public void scale(float size) {
        scaleM(modelMatrix, 0, size, size, size);
    }

    public void scaleX(float size) {
        scaleM(modelMatrix, 0, size, 0, 0);
    }

    public void scaleY(float size) {
        scaleM(modelMatrix, 0, 0, size, 0);
    }

    public void scaleZ(float size) {
        scaleM(modelMatrix, 0, 0, 0, size);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float[] getModelMatrix() {
        return modelMatrix;
    }



}
