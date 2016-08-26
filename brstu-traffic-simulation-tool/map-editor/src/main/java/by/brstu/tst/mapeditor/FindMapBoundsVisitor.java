package by.brstu.tst.mapeditor;

import by.brstu.tst.core.map.elements.*;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.MapRectangle;

/**
 * Created by a.klimovich on 26.08.2016.
 */
public class FindMapBoundsVisitor extends BaseRoadElementVisitor {
    float minLon;
    float maxLon;
    float minLat;
    float maxLat;
    boolean firstVisitFlag;

    public FindMapBoundsVisitor() {
        firstVisitFlag = true;
    }

    @Override
    public void visit(NodeRoadElement roadElement) {
        MapPoint point = roadElement.getBasePoint();
        if (firstVisitFlag) {
            minLon = maxLon = point.getLon();
            minLat = maxLat = point.getLat();
            firstVisitFlag = false;
        }
        else {
            minLon = Math.min(minLon, point.getLon());
            maxLon = Math.max(maxLon, point.getLon());
            minLat = Math.min(minLat, point.getLat());
            maxLat = Math.max(maxLat, point.getLat());
        }
    }

    public MapRectangle getMapBounds() {
        return new MapRectangle(minLon, minLat, maxLon, maxLat);
    }
}
