package by.brstu.tst.core.map.primitives;


import junit.framework.TestCase;

/**
 * Created by a.klimovich on 11.09.2016.
 */
public class VectorTest extends TestCase {
    public void testAddToPoint() {
        MapPoint point = new MapPoint(2637887.25, 6815777.50);
        Vector vector = new Vector(600, 800).setLength(1);
        MapPoint newPoint = vector.clone().multiply(0.1).addToPoint(point);
        assertEquals(2637887.31, newPoint.getX(), 0.001);
        assertEquals(6815777.58, newPoint.getY(), 0.001);
    }

    public void testSetLength() {
        Vector vector = new Vector(3, 4);
        vector.setLength(0.1);
        assertEquals(0.1, vector.getLength(), 0.001);
    }

    public void testMultiply() {
        Vector vector = new Vector(3, 4);
        vector.multiply(0.1);
        assertEquals(0.5, vector.getLength(), 0.001);
    }
}
