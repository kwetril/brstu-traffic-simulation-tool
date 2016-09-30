package by.brstu.tst.core.map.utils;

import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Created by a.klimovich on 27.08.2016.
 */
public class MapUtils {
    private GeodeticCalculator distanceCalculator;

    public MapUtils() {
        try {
            CoordinateReferenceSystem crs = CRS.decode("EPSG:3857");
            distanceCalculator = new GeodeticCalculator(crs);
        } catch (Exception ex) {
            distanceCalculator = null;
            return;
        }
    }

    /**
     * Returns distance between two points in meters.
     */
    public double calculateDistance(MapPoint startPoint, MapPoint destinationPoint) {
        distanceCalculator.setStartingGeographicPoint(startPoint.getX(), startPoint.getY());
        distanceCalculator.setDestinationGeographicPoint(destinationPoint.getX(), destinationPoint.getY());
        return distanceCalculator.getOrthodromicDistance();
    }

    public static MapPoint GetLeftPoint(MapPoint basePoint, Vector baseDirection, float distance) {
        Vector leftVector = baseDirection.clone().turnLeft();
        leftVector = leftVector.setLength(distance);
        return leftVector.addToPoint(basePoint);
    }

    public static MapPoint GetRightPoint(MapPoint basePoint, Vector baseDirection, float distance) {
        Vector rightVector = baseDirection.clone().turnRight();
        rightVector = rightVector.setLength(distance);
        return rightVector.addToPoint(basePoint);
    }

    public static BezierCurve moveCurveLeft(BezierCurve curve, double distance) {
        if (distance < 0.001) {
            return curve;
        }

        MapPoint[] points = curve.getPoints();
        MapPoint[] newPoints = new MapPoint[points.length];

        Vector v1 = new Vector(points[0], points[1]);
        v1.turnLeft().setLength(distance);
        Vector v2 = new Vector(points[1], points[2]);
        v2.turnLeft().setLength(distance);
        Vector v3 = new Vector(points[2], points[3]);
        v3.turnLeft().setLength(distance);

        newPoints[0] = v1.addToPoint(points[0]);
        newPoints[3] = v3.addToPoint(points[3]);

        double cos12 = Math.min(v1.scalarMultiply(v2) / v1.getLength() / v2.getLength(), 1.0);
        double alpha12 = Math.acos(cos12);
        double cos23 = Math.min(v2.scalarMultiply(v3) / v2.getLength() / v3.getLength(), 1.0);
        double alpha23 = Math.acos(cos23);

        newPoints[1] = v1.clone().add(v2).multiply(0.5 / Math.cos(alpha12 / 2)).addToPoint(points[1]);
        newPoints[2] = v2.clone().add(v3).multiply(0.5 / Math.cos(alpha23 / 2)).addToPoint(points[2]);

        BezierCurve result = new BezierCurve(newPoints[0], newPoints[1], newPoints[2], newPoints[3]);
        return result;
    }

    public static BezierCurve moveCurveRight(BezierCurve curve, double distance) {
        if (distance < 0.001) {
            return curve;
        }

        MapPoint[] points = curve.getPoints();
        MapPoint[] newPoints = new MapPoint[points.length];

        Vector v1 = new Vector(points[0], points[1]);
        v1.turnRight().setLength(distance);
        Vector v2 = new Vector(points[1], points[2]);
        v2.turnRight().setLength(distance);
        Vector v3 = new Vector(points[2], points[3]);
        v3.turnRight().setLength(distance);

        newPoints[0] = v1.addToPoint(points[0]);
        newPoints[3] = v3.addToPoint(points[3]);

        double cos12 = Math.min(v1.scalarMultiply(v2) / v1.getLength() / v2.getLength(), 1.0);
        double alpha12 = Math.acos(cos12);
        double cos23 = Math.min(v2.scalarMultiply(v3) / v2.getLength() / v3.getLength(), 1.0);
        double alpha23 = Math.acos(cos23);

        newPoints[1] = v1.clone().add(v2).multiply(0.5 / Math.cos(alpha12 / 2)).addToPoint(points[1]);
        newPoints[2] = v2.clone().add(v3).multiply(0.5 / Math.cos(alpha23 / 2)).addToPoint(points[2]);

        BezierCurve result = new BezierCurve(newPoints[0], newPoints[1], newPoints[2], newPoints[3]);
        return result;
    }
}
