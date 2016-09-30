package by.brstu.tst.core.map.elements;

import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;

/**
 * Created by kwetril on 8/17/16.
 */
public class Intersection extends NodeRoadElement {
    public Intersection(String name, MapPoint basePoint) {
        super(name, basePoint);
    }

    @Override
    public void accept(BaseRoadElementVisitor visitor) {
        visitor.visit(this);
    }

    public BezierCurve getConnector(DirectedRoad from, int laneFrom, DirectedRoad to, int laneTo) {
        RoadSegment[] fromSegments = from.getSegments();
        RoadSegment fromSegment = fromSegments[fromSegments.length - 1];
        MapPoint[] fromPoints = fromSegment.getLane(laneFrom).getCurve().getPoints();

        MapPoint[] connectorPoints = new MapPoint[4];
        connectorPoints[0] = fromPoints[3];
        connectorPoints[1] = new Vector(fromPoints[2], fromPoints[3]).setLength(1).addToPoint(fromPoints[3]);

        RoadSegment[] toSegments = to.getSegments();
        RoadSegment toSegment = toSegments[0];
        MapPoint[] toPoints = toSegment.getLane(laneTo).getCurve().getPoints();

        connectorPoints[3] = toPoints[0];
        connectorPoints[2] = new Vector(toPoints[1], toPoints[0]).setLength(1).addToPoint(toPoints[0]);
        return new BezierCurve(connectorPoints[0], connectorPoints[1],
                connectorPoints[2], connectorPoints[3]);
    }
}
