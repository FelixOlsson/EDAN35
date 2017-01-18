package android.ahaonline.com.edan35.util;



/**
 * Created by Felix on 2016-12-16.
 */



public class Geometry {



    public static class Vec3 {
        public final float x, y, z;

        public Vec3(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vec3(Vec3 from, Vec3 to) {
            this.x = to.x - from.x;
            this.y = to.y - from.y;
            this.z = to.z - from.z;
        }


        public Vec3 translate(Vec3 vec3) {
            return new Vec3(
                    x + vec3.x,
                    y + vec3.y,
                    z + vec3.z);
        }


        public float dotProduct(Vec3 other) {
            return x * other.x
                    + y * other.y
                    + z * other.z;
        }

        public Vec3 scale(float f) {
            return new Vec3(
                    x * f,
                    y * f,
                    z * f);
        }
    }

    public static class Ray {
        public final Vec3 point;
        public final Vec3 vector;

        public Ray(Vec3 point, Vec3 vec3) {
            this.point = point;
            this.vector = vec3;
        }
    }


    public static class Plane {
        public final Vec3 point;
        public final Vec3 normal;

        public Plane(Vec3 point, Vec3 normal) {
            this.point = point;
            this.normal = normal;
        }
    }



    public static Vec3 intersectionRayPlane(Ray ray, Plane plane) {

        float t = new Vec3(ray.point, plane.point).dotProduct(plane.normal)
                / ray.vector.dotProduct(plane.normal);

        Vec3 intersectionPoint = ray.point.translate(ray.vector.scale(t));
        return intersectionPoint;
    }


    private  static float distance(Vec3 v) {
        return (float)Math.sqrt(v.x * v.x +
                v.y * v.y + v.z * v.z);
    }

    public static boolean intersectionPointSphere(float x, float y, float spX, float spY, float threshold) {
        return ((x - spX) * (x - spX) +
                (y - spY) * (y - spY) < threshold);

    }

    public static float distancePointToPoint(Vec3 from, Vec3 to) {
        return (float)Math.sqrt((to.x - from.x) * (to.x - from.x) +
                (to.y - from.y) * (to.y - from.y) + (to.z - from.z) * (to.z - from.z));
    }
}