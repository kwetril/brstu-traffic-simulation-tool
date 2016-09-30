package by.brstu.tst.core.map.primitives;

/**
 * Created by a.klimovich on 28.09.2016.
 */
public class BezierCurve {
    private MapPoint[] points;

    public BezierCurve(MapPoint pt1, MapPoint pt2, MapPoint pt3, MapPoint pt4) {
        points = new MapPoint[4];
        points[0] = pt1;
        points[1] = pt2;
        points[2] = pt3;
        points[3] = pt4;
    }

    public MapPoint[] getPoints() {
        return points;
    }

    public MapPoint getPoint(double t) {
        double tpow2 = t * t;
        double tpow3 = tpow2 * t;
        double t1 = (1 - t);
        double t1pow2 = t1 * t1;
        double t1pow3 = t1pow2 * t1;

        double x = t1pow3 * points[0].getX() + 3 * t * t1pow2 * points[1].getX()
                + 3 * tpow2 * t1 * points[2].getX() + tpow3 * points[3].getX();
        double y = t1pow3 * points[0].getY() + 3 * t * t1pow2 * points[1].getY()
                + 3 * tpow2 * t1 * points[2].getY() + tpow3 * points[3].getY();
        return new MapPoint(x, y);
    }

    public Vector getDirection(double t) {
        double tpow2 = t * t;
        double xa = points[3].getX() - 3 * points[2].getX() + 3 * points[1].getX() - points[0].getX();
        double xb = 3 * points[2].getX() - 6 * points[1].getX() + 3 * points[0].getX();
        double xc = 3 * points[1].getX() - 3 * points[0].getX();
        double x = 3 * xa * tpow2 + 2 * xb * t + xc;

        double ya = points[3].getY() - 3 * points[2].getY() + 3 * points[1].getY() - points[0].getY();
        double yb = 3 * points[2].getY() - 6 * points[1].getY() + 3 * points[0].getY();
        double yc = 3 * points[1].getY() - 3 * points[0].getY();
        double y = 3 * ya * tpow2 + 2 * yb * t + yc;
        return new Vector(x, y).setLength(1.0);
    }
}
