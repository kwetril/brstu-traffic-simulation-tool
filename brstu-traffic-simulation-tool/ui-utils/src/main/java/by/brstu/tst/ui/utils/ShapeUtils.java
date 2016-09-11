package by.brstu.tst.ui.utils;

import by.brstu.tst.core.map.primitives.MapPoint;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

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
}
