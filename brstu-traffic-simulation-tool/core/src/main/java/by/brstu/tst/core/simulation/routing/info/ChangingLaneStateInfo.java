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
public class ChangingLaneStateInfo extends RouteStateInfo {
    public ChangingLaneStateInfo(Route route) {
        super(route);
    }

    @Override
    public int getLane() {
        return 0;
    }

    @Override
    public int getLaneAfterNode() {
        return 0;
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
        return 0;
    }

    @Override
    public double getCurveParameter() {
        return 0;
    }

    @Override
    public MapPoint getPosition() {
        return null;
    }

    @Override
    public Vector getDirection() {
        return null;
    }

    @Override
    public boolean isChangingLane() {
        return false;
    }

    @Override
    public int getLaneAfterChange() {
        return 0;
    }

    @Override
    public int getSegmentAfterLaneChange() {
        return 0;
    }

    @Override
    public double getCurveParameterAfterLaneChange() {
        return 0;
    }

    @Override
    public int getNumLanes() {
        return 0;
    }
}
