package by.brstu.tst.core.map.elements;

import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;

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

    public BezierCurve getConnector(RoadConnectorDescription connectorDescription) {
        return getConnector(connectorDescription.getFrom(), connectorDescription.getFromLane(),
                connectorDescription.getTo(), connectorDescription.getToLane());
    }

    public BezierCurve getConnector(DirectedRoad from, int laneFrom, DirectedRoad to, int laneTo) {
        RoadSegment[] fromSegments = from.getSegments();
        RoadSegment fromSegment = fromSegments[fromSegments.length - 1];
        MapPoint[] fromPoints = fromSegment.getLane(laneFrom).getCurve().getPoints();

        RoadSegment[] toSegments = to.getSegments();
        RoadSegment toSegment = toSegments[0];
        MapPoint[] toPoints = toSegment.getLane(laneTo).getCurve().getPoints();

        MapPoint startPoint = fromPoints[3];
        Vector startDirection = new Vector(fromPoints[2], fromPoints[3]).setLength(1);

        MapPoint endPoint = toPoints[0];
        Vector endDirection = new Vector(toPoints[0], toPoints[1]).setLength(1);

        Vector startToEnd = new Vector(startPoint, endPoint);

        double startProjection = startToEnd.scalarMultiply(startDirection) * 0.5;
        MapPoint secondPoint = startDirection.setLength(startProjection).addToPoint(startPoint);

        double endProjection = startToEnd.scalarMultiply(endDirection) * 0.5;
        MapPoint thirdPoint = endDirection.setLength(endProjection).multiply(-1).addToPoint(endPoint);


        return new BezierCurve(startPoint, secondPoint, thirdPoint, endPoint);
    }
}
