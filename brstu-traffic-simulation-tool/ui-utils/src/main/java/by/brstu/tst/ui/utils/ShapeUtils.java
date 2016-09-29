package by.brstu.tst.ui.utils;

import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

/**
 * Created by a.klimovich on 11.09.2016.
 */
public class ShapeUtils {
    public static Line2D.Float lineFromPoints(MapPoint startPoint, MapPoint endPoint) {
        return new Line2D.Float((float) startPoint.getX(), (float) startPoint.getY(),
                (float) endPoint.getX(), (float) endPoint.getY());
    }

    public static Ellipse2D.Float circleFromPoint(MapPoint point, float radius) {
        return new Ellipse2D.Float((float) point.getX() - radius,
                (float) point.getY() - radius, 2 * radius, 2 * radius);
    }

    public static Path2D.Float updatePathWithBezierCurve(Path2D.Float path, BezierCurve curve) {
        MapPoint[] points = curve.getPoints();
        path.moveTo(points[0].getX(), points[0].getY());
        path.curveTo(points[1].getX(), points[1].getY(),
                points[2].getX(), points[2].getY(),
                points[3].getX(), points[3].getY());
        return path;
    }
}
