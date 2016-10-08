package by.brstu.tst.core.simulation.routing.info;

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
public class InnerNodeStateInfo extends RouteStateInfo {
    private Intersection node;
    private BezierCurve curve;
    private double curveParameter;
    private int laneTo;

    public InnerNodeStateInfo(Route route, Intersection node, int laneTo, BezierCurve curve, double curveParameter) {
        super(route);
        this.node = node;
        this.curve = curve;
        this.curveParameter = curveParameter;
        this.laneTo = laneTo;
    }

    @Override
    public int getLane() {
        throw new IllegalStateException("Can't get lane when not on road");
    }

    @Override
    public int getLaneAfterNode() {
        return laneTo;
    }

    @Override
    public boolean isOnRoad() {
        return false;
    }

    @Override
    public Intersection getNextIntersection() {
        return null;
    }

    @Override
    public DirectedRoad getCurrentRoad() {
        throw new IllegalStateException("Can't get road when not on road");
    }

    @Override
    public DirectedRoad getNextRoad() {
        return route.getNextRoad(node);
    }

    @Override
    public NodeRoadElement getNextNode() {
        return route.getNextNode(getNextRoad());
    }

    @Override
    public int getCurrentSegment() {
        throw new IllegalStateException("Can't get segment when not on road");
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
        return false;
    }

    @Override
    public int getLaneAfterChange() {
        throw new IllegalStateException("Can't get lane after change when not changing lane");
    }

    @Override
    public int getSegmentAfterLaneChange() {
        throw new IllegalStateException("Can't get segment after lane change when not changing lane");
    }

    @Override
    public double getCurveParameterAfterLaneChange() {
        throw new IllegalStateException("Can't get curve parameter after lane change when not changing lane");
    }

    @Override
    public int getNumLanes() {
        throw new IllegalStateException("Can't get number of lanes when not on road");
    }
}
