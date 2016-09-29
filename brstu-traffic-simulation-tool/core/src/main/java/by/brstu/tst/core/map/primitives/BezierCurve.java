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
}
