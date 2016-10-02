package by.brstu.tst.core.simulation.routing;

import by.brstu.tst.core.map.elements.*;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;

/**
 * Created by a.klimovich on 30.09.2016.
 */
public class RouteState {
    private Route route;
    int lane;
    BezierCurve currentCurve;
    double curveParameter;
    double curveParameterDelta;
    int currentSegment;


    boolean isOnRoad;
    NodeRoadElement curNode;
    DirectedRoad curRoad;
    NodeRoadElement nextNode;
    DirectedRoad nextRoad;

    MapPoint position;
    boolean reachDestination = false;


    public RouteState(Route route, int initialLane) {
        this.route = route;
        curveParameterDelta = 0.01;
        NodeRoadElement source = route.getSource();
        DirectedRoad currentRoad = route.getNextRoad(source);
        currentSegment = 0;
        currentCurve = currentRoad.getSegments()[currentSegment].getLane(initialLane).getCurve();
        position = currentCurve.getPoints()[0];
        curveParameter = 0;
        isOnRoad = true;
        curNode = null;
        curRoad = currentRoad;
        nextRoad = null;
        nextNode = route.getNextNode(curRoad);
        lane = initialLane;
    }

    public void updatePosition(double distance) {
        while (distance > 0) {
            while (curveParameter < 1.0 && distance > 0) {
                double nextCurveParameter = Math.min(curveParameter + curveParameterDelta, 1.0);
                MapPoint nextPosition = currentCurve.getPoint(nextCurveParameter);
                double distanceToNextPosition = position.distanceTo(nextPosition);
                if (distance < distanceToNextPosition) {
                    curveParameter = Math.min(curveParameter + distance / distanceToNextPosition * curveParameterDelta,
                            1.0);
                    distance = 0;
                }
                else {
                    curveParameter = nextCurveParameter;
                    distance -= distanceToNextPosition;
                }
                position = currentCurve.getPoint(curveParameter);
            }
            if (distance > 0) {
                goToNextCurve();
                curveParameter = 0;
                position = currentCurve.getPoints()[0];
            }
            if (reachDestination) {
                break;
            }
        }
    }

    private void goToNextCurve() {
        if (isOnRoad) {
            RoadSegment[] roadSegments = curRoad.getSegments();
            if (currentSegment < roadSegments.length - 1) {
                currentSegment++;
                currentCurve = roadSegments[currentSegment].getLane(lane).getCurve();
            }
            else {
                goToNextNode();
            }
        }
        else {
            goToNextRoad();
        }
    }

    private void goToNextRoad() {
        isOnRoad = true;
        curRoad = nextRoad;
        curNode = null;
        nextRoad = null;
        nextNode = route.getNextNode(curRoad);

        lane = Math.min(lane, curRoad.getNumLanes() - 1);
        currentSegment = 0;
        currentCurve = curRoad.getSegments()[currentSegment].getLane(lane).getCurve();
    }

    private void goToNextNode() {
        isOnRoad = false;
        curNode = nextNode;
        nextRoad = route.getNextRoad(curNode);
        nextNode = null;
        if (nextRoad == null) {
            reachDestination = true;
            return;
        } else {
            int newLane = Math.min(lane, nextRoad.getNumLanes() - 1);
            currentCurve = ((Intersection)curNode).getConnector(curRoad, lane, nextRoad, newLane);
            lane = newLane;
        }
        curRoad = null;
    }

    public MapPoint getPosition() {
        return position;
    }

    public Vector getDirection() {
        return currentCurve.getDirection(curveParameter);
    }

    public boolean reachedDestination() {
        return reachDestination;
    }

    public boolean isBeforeIntersection() {
        return isOnRoad && nextNode instanceof Intersection;
    }

    public Intersection getNextIntersection() {
        BaseRoadElement position;
        if (isOnRoad) {
            position = curRoad;
        }
        else {
            position = nextRoad;
        }
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

    public int getCurrentLane() {
        return lane;
    }

    public DirectedRoad getCurrentRoad() {
        return curRoad;
    }

    public DirectedRoad getNextRoad() {
        return route.getNextRoad(nextNode);
    }
}
