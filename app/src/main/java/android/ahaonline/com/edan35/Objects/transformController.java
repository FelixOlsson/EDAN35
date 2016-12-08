package android.ahaonline.com.edan35.Objects;

import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by Felix on 2016-11-21.
 */

public class transformController {

    private float x, y, z;
    private float rotationX, rotationY, rotationZ;
    private final float[] modelMatrix = new float[16];

    //temporary values
    private float tX = 0, tY = 0, tZ = 0;
    private float tRotationX = 0, tRotationY = 0, tRotationZ = 0;
    private float tSizeX = 1, tSizeY = 1, tSizeZ = 1;

    public transformController() {
        setIdentityM(modelMatrix, 0);
    }


    public void translate(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        tX = x;
        tY = y;
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

    public void transformMatrix() {
        scaleM(modelMatrix, 0, tSizeX, tSizeY, tSizeZ);
        rotateM(modelMatrix, 0, tRotationX, 1f, 0f, 0f);
        rotateM(modelMatrix, 0, tRotationY, 0f, 1f, 0f);
        rotateM(modelMatrix, 0, tRotationZ, 0f, 0f, 1f);
        translateM(modelMatrix, 0, tX, tY, tZ);

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
