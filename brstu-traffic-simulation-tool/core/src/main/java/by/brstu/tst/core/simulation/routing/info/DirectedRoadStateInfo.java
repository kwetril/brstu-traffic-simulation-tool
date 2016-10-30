package by.brstu.tst.core.simulation.routing.info;

import by.brstu.tst.core.map.elements.BaseRoadElement;
import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.elements.NodeRoadElement;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.simulation.routing.Route;

/**
 * Created by a.klimovich on 09.10.2016.
 */
public class DirectedRoadStateInfo extends RouteStateInfo {
    private DirectedRoad road;
    private int lane;
    private int segment;
    private double curveParameter;


    public DirectedRoadStateInfo(Route route, DirectedRoad road, int lane, int segment, double curveParameter) {
        super(route);
        this.road = road;
        this.lane = lane;
        this.segment = segment;
        this.curveParameter = curveParameter;
    }

    @Override
    public int getLane() {
        return lane;
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
        NodeRoadElement nextNode = route.getNextNode(road);
        return route.getNextRoad(nextNode);
    }

    @Override
    public NodeRoadElement getNextNode() {
        return route.getNextNode(road);
    }

    @Override
    public int getCurrentSegment() {
        return segment;
    }

    @Override
    public double getCurveParameter() {
        return curveParameter;
    }

    @Override
    public MapPoint getPosition() {
        return road.getSegments()[segment].getLane(lane).getCurve().getPoint(curveParameter);
    }

    @Override
    public Vector getDirection() {
        return road.getSegments()[segment].getLane(lane).getCurve().getDirection(curveParameter);
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
    public Route getRoute() {
        return route;
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
