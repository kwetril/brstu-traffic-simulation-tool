package by.brstu.tst.core.map.primitives;

/**
 * Created by a.klimovich on 28.08.2016.
 */
public class Vector implements Cloneable {
    private double x;
    private double y;

    public Vector(MapPoint startPoint, MapPoint endPoint) {
        x = endPoint.getX() - startPoint.getX();
        y = endPoint.getY() - startPoint.getY();
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector normalize() {
        double length = getLength();
        x /= length;
        y /= length;
        return this;
    }

    public Vector turnLeft() {
        double newX = -y;
        y = x;
        x = newX;
        return this;
    }

    public Vector turnRight() {
        double newX = y;
        y = -x;
        x = newX;
        return this;
    }

    public Vector setLength(double length) {
        double oldLength = getLength();
        double coefficient = length / oldLength;
        x *= coefficient;
        y *= coefficient;
        return this;
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public double scalarMultiply(Vector vector) {
        return x * vector.x + y * vector.y;
    }

    public Vector multiply(double value) {
        x *= value;
        y *= value;
        return this;
    }

    public Vector add(Vector vector) {
        x += vector.x;
        y += vector.y;
        return this;
    }

    public double angleClockwise(Vector anotherVector) {
        double crossProduct = scalarMultiply(anotherVector);
        double angle = Math.acos(crossProduct / getLength() / anotherVector.getLength());
        double vectorProduct = x * anotherVector.y - anotherVector.x * y;
        if (vectorProduct > 0) {
            angle = 2 * Math.PI - angle;
        }
        return angle;
    }

    @Override
    public String toString() {
        return String.format("x: %s; y: %s", x, y);
    }

    @Override
    public Vector clone() {
        return new Vector(x, y);
    }

    public MapPoint addToPoint(MapPoint point) {
        return new MapPoint(point.getX() + x, point.getY() + y);
    }
}
