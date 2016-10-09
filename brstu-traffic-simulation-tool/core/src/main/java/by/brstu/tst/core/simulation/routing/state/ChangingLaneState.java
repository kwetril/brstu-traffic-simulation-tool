package by.brstu.tst.core.simulation.routing.state;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.simulation.routing.Route;
import by.brstu.tst.core.simulation.routing.info.ChangingLaneStateInfo;
import by.brstu.tst.core.simulation.routing.info.RouteStateInfo;

/**
 * Created by a.klimovich on 08.10.2016.
 */
public class ChangingLaneState extends RouteInnerState {
    private Route route;
    private DirectedRoad road;
    private int startLane;
    private int startSegment;
    private int endLane;
    private int endSegment;
    private double startCurveParameter;
    private double endCurveParameter;

    public ChangingLaneState(Route route, DirectedRoad road, int startLane, int segment, double curveParameter,
                             ChangeLaneType changeLaneType, double distance) {
        this.route = route;
        this.road = road;
        this.startLane = startLane;
        if (changeLaneType == ChangeLaneType.LEFT) {
            this.endLane = this.startLane - 1;
        } else {
            this.endLane = this.startLane + 1;
        }
        this.startSegment = segment;
        this.startCurveParameter = curveParameter;
        this.curveParameter = 0.0;
        calculateEndSegmentAndParameter(distance);
    }

    private void calculateEndSegmentAndParameter(double distance) {
        BezierCurve startCurve = road.getSegments()[startSegment].getLane(startLane).getCurve();
        MapPoint startPoint = startCurve.getPoint(startCurveParameter);
        Vector startDirection = startCurve.getDirection(startCurveParameter);

        endCurveParameter = startCurveParameter;
        endSegment = startSegment;
        BezierCurve endCurve = road.getSegments()[endSegment].getLane(endLane).getCurve();
        while (endCurveParameter < 1.0 && distance > EPS) {
            double curveParameterDelta = 0.01;
            double nextCurveParameter = Math.min(endCurveParameter + curveParameterDelta, 1.0);
            MapPoint currentPosition = endCurve.getPoint(endCurveParameter);
            MapPoint nextPosition = endCurve.getPoint(nextCurveParameter);
            double distanceToNextPosition = currentPosition.distanceTo(nextPosition);
            if (distance < distanceToNextPosition) {
                endCurveParameter = Math.min(endCurveParameter + distance / distanceToNextPosition * curveParameterDelta,
                        1.0);
                distance = 0;
            } else {
                endCurveParameter = nextCurveParameter;
                distance -= distanceToNextPosition;
            }
            if (endCurveParameter >= 1.0 && endSegment + 1 < road.getSegments().length) {
                endSegment++;
                endCurveParameter = 0.0;
                endCurve = road.getSegments()[endSegment].getLane(endLane).getCurve();
            }
        }

        MapPoint endPoint = endCurve.getPoint(endCurveParameter);
        Vector endDirection = endCurve.getDirection(endCurveParameter);

        Vector startToEnd = new Vector(startPoint, endPoint);
        double startProjection = startToEnd.scalarMultiply(startDirection) * 0.5;
        MapPoint secondPoint = startDirection.setLength(startProjection).addToPoint(startPoint);
        double endProjection = startToEnd.scalarMultiply(endDirection) * 0.5;
        MapPoint thirdPoint = endDirection.setLength(endProjection).multiply(-1).addToPoint(endPoint);

        curve = new BezierCurve(startPoint, secondPoint, thirdPoint, endPoint);
    }

    @Override
    public double updateState(double distance) {
        return updateStateOnCurve(distance);
    }

    @Override
    public RouteStateInfo getStateInfo() {
        return new ChangingLaneStateInfo(route, road, startLane, startSegment,
                endLane, endSegment, endCurveParameter, curve, curveParameter);
    }
}
