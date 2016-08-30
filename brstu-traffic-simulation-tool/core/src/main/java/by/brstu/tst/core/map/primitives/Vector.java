package by.brstu.tst.core.map.primitives;

/**
 * Created by a.klimovich on 28.08.2016.
 */
public class Vector implements Cloneable {
    private float x;
    private float y;

    public Vector(MapPoint startPoint, MapPoint endPoint) {
        x = endPoint.getX() - startPoint.getX();
        y = endPoint.getY() - startPoint.getY();
    }

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector normalize() {
        float length = getLength();
        x /= length;
        y /= length;
        return this;
    }

    public Vector turnLeft() {
        float newX = -y;
        y = x;
        x = newX;
        return this;
    }

    public Vector turnRight() {
        float newX = y;
        y = -x;
        x = newX;
        return this;
    }

    public Vector setLength(float length) {
        float oldLength = getLength();
        float coefficient = length / oldLength;
        x *= coefficient;
        y *= coefficient;
        return this;
    }

    public float getLength() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float scalarMultiply(Vector vector) {
        return x * vector.x + y * vector.y;
    }

    public Vector multiply(float value) {
        x *= value;
        y *= value;
        return this;
    }

    public float angleClockwise(Vector anotherVector) {
        float crossProduct = scalarMultiply(anotherVector);
        float angle = (float) Math.acos(crossProduct / getLength() / anotherVector.getLength());
        float vectorProduct = x * anotherVector.y - anotherVector.x * y;
        if (vectorProduct > 0) {
            angle = (float) (2 * Math.PI - angle);
        }
        return angle;
    }

    @Override
    public Vector clone() {
        return new Vector(x, y);
    }

    public MapPoint addToPoint(MapPoint point) {
        return new MapPoint(point.getX() + x, point.getY() + y);
    }
}
