package android.ahaonline.com.edan35.Objects;

import static android.R.attr.rotation;
import static android.R.attr.x;

/**
 * Created by Felix on 2016-11-21.
 */

public class AbstractObject {
    protected float x, y, z;
    protected float rotationX, rotationY, rotationZ;
    protected final float[] modelMatrix = new float[16];


    public void translate(float x, float y, float z) {

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
