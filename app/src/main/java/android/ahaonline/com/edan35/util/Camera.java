package android.ahaonline.com.edan35.util;

import android.opengl.Matrix;
import static java.lang.Math.*;

/**
 * Created by felix on 09/12/2016.
 */
public class Camera {


    private static final float[] viewMatrix = new float[16];

    public Camera() {

    }

    private float[] normalize(float[] vector) {
        float length = (float)sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);
        vector[0] = vector[0]/length;
        vector[1] = vector[1]/length;
        vector[2] = vector[2]/length;

        return vector;
    }

    private float[] cross(float[] v1, float[] v2) {
        float[] result = new float[3];
        result[0] = v1[1] * v2[2] - v2[1] * v1[2];
        result[1] = v1[2] * v2[0] - v2[2] * v1[0];
        result[2] = v1[0] * v2[1] - v2[0] * v1[1];

        return result;
    }

}
