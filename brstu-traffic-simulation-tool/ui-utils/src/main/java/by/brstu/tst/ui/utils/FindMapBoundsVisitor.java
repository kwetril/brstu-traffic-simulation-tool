package by.brstu.tst.ui.utils;

import by.brstu.tst.core.map.elements.*;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.MapRectangle;

/**
 * Created by a.klimovich on 26.08.2016.
 */
public class FindMapBoundsVisitor extends BaseRoadElementVisitor {
    double minLon;
    double maxLon;
    double minLat;
    double maxLat;
    boolean firstVisitFlag;

    public FindMapBoundsVisitor() {
        firstVisitFlag = true;
    }

    @Override
    public void visit(NodeRoadElement roadElement) {
        MapPoint point = roadElement.getBasePoint();
        if (firstVisitFlag) {
            minLon = maxLon = point.getX();
            minLat = maxLat = point.getY();
            firstVisitFlag = false;
        }
        else {
            minLon = Math.min(minLon, point.getX());
            maxLon = Math.max(maxLon, point.getX());
            minLat = Math.min(minLat, point.getY());
            maxLat = Math.max(maxLat, point.getY());
        }
    }

    @Override
    public void visit(DirectedRoad road) {
        for (BezierCurve curve : road.getSegments()) {
            for (MapPoint point : curve.getPoints()) {
                if (firstVisitFlag) {
                    minLon = maxLon = point.getX();
                    minLat = maxLat = point.getY();
                    firstVisitFlag = false;
                }
                else {
                    minLon = Math.min(minLon, point.getX());
                    maxLon = Math.max(maxLon, point.getX());
                    minLat = Math.min(minLat, point.getY());
                    maxLat = Math.max(maxLat, point.getY());
                }
            }
        }
    }

    public MapRectangle getMapBounds() {
        System.out.printf("%s %s %s %s\n", minLon, minLat, maxLon, maxLat);
        return new MapRectangle(minLon, minLat, maxLon, maxLat);
    }
}
