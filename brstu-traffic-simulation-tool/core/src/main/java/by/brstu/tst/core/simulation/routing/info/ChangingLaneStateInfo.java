package by.brstu.tst.core.simulation.routing.info;

import by.brstu.tst.core.map.elements.BaseRoadElement;
import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.elements.NodeRoadElement;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.simulation.routing.Route;

/**
 * Created by a.klimovich on 09.10.2016.
 */
public class ChangingLaneStateInfo extends RouteStateInfo {
    private Route route;
    private DirectedRoad road;
    private int startLane;
    private int startSegment;
    private int endLane;
    private int endSegment;
    private BezierCurve curve;
    private double curveParameter;
    private double endCurveParameter;

    public ChangingLaneStateInfo(Route route, DirectedRoad road,
                                 int startLane, int startSegment,
                                 int endLane, int endSegment, double endCurveParameter,
                                 BezierCurve curve, double curveParameter) {
        super(route);
        this.route = route;
        this.road = road;
        this.startLane = startLane;
        this.startSegment = startSegment;
        this.endLane = endLane;
        this.endSegment = endSegment;
        this.endCurveParameter = endCurveParameter;
        this.curve = curve;
        this.curveParameter = curveParameter;
    }

    @Override
    public int getLane() {
        return startLane;
    }

    @Override
    public int getLaneAfterNode() {
        throw new IllegalStateException("Can't get lane after node when on road");
    }

    @Override
    public boolean isOnRoad() {
        return true;
    }

    @Override
    public Intersection getNextIntersection() {
        BaseRoadElement position;
        position = road;
        boolean posOnRoad = true;
        while (position != null) {
            if (posOnRoad) {
                position = route.getNextNode((DirectedRoad) position);
            }
            else {
                if (position instanceof Intersection) {
                    return (Intersection) position;
                }
                position = route.getNextRoad((NodeRoadElement) position);
            }
            posOnRoad = !posOnRoad;
        }
        return null;
    }

    @Override
    public DirectedRoad getCurrentRoad() {
        return road;
    }

    @Override
    public DirectedRoad getNextRoad() {
        return route.getNextRoad(getNextNode());
    }

    @Override
    public NodeRoadElement getNextNode() {
        return route.getNextNode(road);
    }

    @Override
    public int getCurrentSegment() {
        return startSegment;
    }

    @Override
    public double getCurveParameter() {
        return curveParameter;
    }

    @Override
    public MapPoint getPosition() {
        return curve.getPoint(curveParameter);
    }

    @Override
    public Vector getDirection() {
        return curve.getDirection(curveParameter);
    }

    @Override
    public boolean isChangingLane() {
        return true;
    }

    @Override
    public int getLaneAfterChange() {
        return endLane;
    }

    @Override
    public int getSegmentAfterLaneChange() {
        return endSegment;
    }

    @Override
    public double getCurveParameterAfterLaneChange() {
        return endCurveParameter;
    }

    @Override
    public int getNumLanes() {
        return road.getNumLanes();
    }

    @Override
    public NodeRoadElement getCurrentNode() {
        throw new IllegalStateException("Can't get current node when on road");
    }
}
