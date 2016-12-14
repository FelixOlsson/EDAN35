package android.ahaonline.com.edan35.util;

import android.graphics.Point;
import android.util.FloatMath;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Created by Felix on 2016-12-11.
 */

public class Geometry {

    public static class Point {
        public final float x, y, z;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

    }

    public static class Vector  {
        public final float x, y, z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

    }


}
