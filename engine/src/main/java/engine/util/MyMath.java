package engine.util;

import org.joml.Math;
import org.joml.Vector2f;

import static org.joml.Math.abs;

public class MyMath {
    public static int GreatestCommonDivisor(int a, int b)
    {
        int h;
        if (a == 0) return abs(b);
        if (b == 0) return abs(a);

        do {
            h = a % b;
            a = b;
            b = h;
        } while (b != 0);

        return abs(a);
    }

    public static Vector2f rotate(Vector2f center, Vector2f point, float angle){
        double s = Math.sin(angle * Math.PI / 180);
        double c = Math.cos(angle * Math.PI / 180);

        // translate point back to origin:
        Vector2f p = new Vector2f(point);
        p.x -= center.x;
        p.y -= center.y;

        // rotate point
        double xnew = p.x * c - p.y * s;
        double ynew = p.x * s + p.y * c;

        // translate point back:
        point.x = (float) xnew + center.x;
        point.y = (float) ynew + center.y;
        return point;
    }
    public static boolean PointRectCol(Vector2f p, Vector2f r1, Vector2f r2){
        // is the point inside the rectangle's bounds?
        if (p.x >= r1.x &&        // right of the left edge AND
                p.x <= r1.x + r2.x &&   // left of the right edge AND
                p.y >= r1.y &&        // below the top AND
                p.y <= r1.y + r2.y) {   // above the bottom
            return true;
        }
        return false;
    }
    public static boolean PointCircle(float px, float py, float cx, float cy, float r) {

        // get distance between the point and circle's center
        // using the Pythagorean Theorem
        float distX = px - cx;
        float distY = py - cy;
        float distance = Math.sqrt( (distX*distX) + (distY*distY) );

        // if the distance is less than the circle's
        // radius the point is inside!
        if (distance <= r) {
            return true;
        }
        return false;
    }
    public static float dist(float px, float py, float x1, float y1){
        return (float) Math.sqrt(java.lang.Math.pow(px - x1, 2) + java.lang.Math.pow(py - y1, 2));
    }

    public static boolean linePoint(float x1, float y1, float x2, float y2, float px, float py) {

        // get distance from the point to the two ends of the line
        float d1 = dist(px,py, x1,y1);
        float d2 = dist(px,py, x2,y2);

        // get the length of the line
        float lineLen = dist(x1,y1, x2,y2);

        // since floats are so minutely accurate, add
        // a little buffer zone that will give collision
        float buffer = 0.5f;    // higher # = less accurate

        // if the two distances are equal to the line's
        // length, the point is on the line!
        // note we use the buffer here to give a range,
        // rather than one #
        if (d1+d2 >= lineLen-buffer && d1+d2 <= lineLen+buffer) {
            return true;
        }
        return false;
    }
}
