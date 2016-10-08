package by.brstu.tst.core.simulation.routing.info;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.elements.NodeRoadElement;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.simulation.routing.Route;

/**
 * Created by a.klimovich on 09.10.2016.
 */
public class DestinationReachedStateInfo extends RouteStateInfo {
    public DestinationReachedStateInfo(Route route) {
        super(route);
    }

    @Override
    public int getLane() {
        throw new IllegalStateException("getLane() not available when destination is reached");
    }

    @Override
    public int getLaneAfterNode() {
        throw new IllegalStateException("Can't get lane after node not available when destination is reached");
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
        return null;
    }

    @Override
    public DirectedRoad getNextRoad() {
        return null;
    }

    @Override
    public NodeRoadElement getNextNode() {
        return null;
    }

    @Override
    public int getCurrentSegment() {
        throw new IllegalStateException("Can't get segment when destination is reached");
    }

    @Override
    public double getCurveParameter() {
        throw new IllegalStateException("Curve parameter not available when destination is reached");
    }

    @Override
    public MapPoint getPosition() {
        return route.getDestination().getBasePoint();
    }

    @Override
    public Vector getDirection() {
        throw new IllegalStateException("Direction not available when destination is reached");
    }

    @Override
    public boolean isChangingLane() {
        return false;
    }

    @Override
    public int getLaneAfterChange() {
        throw new IllegalStateException("Can't get lane after change when destination is reached");
    }

    @Override
    public int getSegmentAfterLaneChange() {
        throw new IllegalStateException("Can't get segment after lane change when destination is reached");
    }

    @Override
    public double getCurveParameterAfterLaneChange() {
        throw new IllegalStateException("Can't get curve parameter after lane change when destination is reached");
    }

    @Override
    public int getNumLanes() {
        throw new IllegalStateException("Can't get number of lanes when destination is reached");
    }

    @Override
    public boolean reachedDestination() {
        return true;
    }

}
