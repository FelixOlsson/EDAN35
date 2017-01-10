package android.ahaonline.com.edan35.util;


/**
 * Created by Felix on 2016-12-16.
 */



public class Geometry {
    public static class Point {
        public final float x, y, z;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point translate(Vector vector) {
            return new Point(
                    x + vector.x,
                    y + vector.y,
                    z + vector.z);
        }
    }

    public static class Vector  {
        public final float x, y, z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }



        public float dotProduct(Vector other) {
            return x * other.x
                    + y * other.y
                    + z * other.z;
        }

        public Vector scale(float f) {
            return new Vector(
                    x * f,
                    y * f,
                    z * f);
        }
    }

    public static class Ray {
        public final Point point;
        public final Vector vector;

        public Ray(Point point, Vector vector) {
            this.point = point;
            this.vector = vector;
        }
    }


    public static class Plane {
        public final Point point;
        public final Vector normal;

        public Plane(Point point, Vector normal) {
            this.point = point;
            this.normal = normal;
        }
    }

    public static Vector vectorBetween(Point from, Point to) {
        return new Vector(to.x - from.x, to.y - from.y, to.z - from.z);
    }

    public static Point intersectionRayPlane(Ray ray, Plane plane) {
        Vector rayToPlaneVector = vectorBetween(ray.point, plane.point);

        float scaleFactor = rayToPlaneVector.dotProduct(plane.normal)
                / ray.vector.dotProduct(plane.normal);

        Point intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor));
        return intersectionPoint;
    }

    public static boolean intersectionPointSphere(float x, float y, float spX, float spY, float threshold) {
        return ((x - spX) * (x - spX) +
                (y - spY) * (y - spY) < threshold);

    }

    public static float distancePointToPoint(Point from, Point to) {
        return (float)Math.sqrt((to.x - from.x) * (to.x - from.x) +
                (to.y - from.y) * (to.y - from.y) + (to.z - from.z) * (to.z - from.z));
    }
}