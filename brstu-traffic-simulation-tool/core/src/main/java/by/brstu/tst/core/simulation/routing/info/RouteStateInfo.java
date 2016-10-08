package by.brstu.tst.core.simulation.routing.info;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.elements.NodeRoadElement;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.simulation.routing.Route;

/**
 * Created by a.klimovich on 08.10.2016.
 */
public abstract class RouteStateInfo {
    protected Route route;

    public RouteStateInfo(Route route) {
        this.route = route;
    }

    public abstract int getLane();
    public abstract int getLaneAfterNode();
    public abstract boolean isOnRoad();
    public abstract Intersection getNextIntersection();
    public abstract DirectedRoad getCurrentRoad();
    public abstract DirectedRoad getNextRoad();
    public abstract NodeRoadElement getNextNode();
    public abstract int getCurrentSegment();
    public abstract double getCurveParameter();
    public abstract MapPoint getPosition();
    public abstract Vector getDirection();
    public abstract boolean isChangingLane();
    public abstract int getLaneAfterChange();
    public abstract int getSegmentAfterLaneChange();
    public abstract double getCurveParameterAfterLaneChange();
    public Route getRoute() {
        return route;
    }
    public abstract int getNumLanes();
    public boolean isBeforeIntersection() {
        return isOnRoad() && getNextNode() instanceof Intersection;
    }
    public boolean reachedDestination() {
        return false;
    }
}
