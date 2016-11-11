package by.brstu.tst.core.map.elements;

import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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

    public HashMap<RoadConnectorDescription, HashSet<RoadConnectorDescription>> findNonConflictConnectors() {
        HashMap<RoadConnectorDescription, HashSet<RoadConnectorDescription>> result = new HashMap<>();
        double laneWidth = Double.MAX_VALUE;
        List<RoadConnectorDescription> allConnectors = new ArrayList<>();
        for (EdgeRoadElement input : getInputElements()) {
            for (EdgeRoadElement output : getOutputElements()) {
                DirectedRoad inputRoad = input.getDirectedRoadByEndNode(this);
                DirectedRoad outputRoad = output.getDirectedRoadByStartNode(this);
                laneWidth = Math.min(laneWidth, Math.min(inputRoad.getLaneWidth(), outputRoad.getLaneWidth()));
                for (int inputLane = 0; inputLane < inputRoad.getNumLanes(); inputLane++) {
                    for (int outputLane = 0; outputLane < outputRoad.getNumLanes(); outputLane++) {
                        RoadConnectorDescription connector = new RoadConnectorDescription(inputRoad, inputLane,
                                outputRoad, outputLane);
                        allConnectors.add(connector);
                        result.put(connector, new HashSet<>());
                    }
                }
            }
        }
        for (int i = 0; i < allConnectors.size(); i++) {
            for (int j = i + 1; j < allConnectors.size(); j++) {
                RoadConnectorDescription first = allConnectors.get(i);
                RoadConnectorDescription second = allConnectors.get(j);
                if (first.getFrom().getName().equals(second.getFrom().getName())
                        && first.getFromLane() == second.getFromLane())  {
                    //non conflict when from the same lane
                    result.get(first).add(second);
                    result.get(second).add(first);
                    continue;
                }
                BezierCurve connector = getConnector(first);
                BezierCurve anotherConnector = getConnector(second);
                double distance = connector.getDistance(anotherConnector);
                boolean connectorsConflict = distance < laneWidth * 0.8;
                if (!connectorsConflict) {
                    result.get(first).add(second);
                    result.get(second).add(first);
                }
            }
        }
        return result;
    }
}
