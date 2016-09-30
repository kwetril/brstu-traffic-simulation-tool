package by.brstu.tst.core.simulation.routing;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.EdgeRoadElement;
import by.brstu.tst.core.map.elements.NodeRoadElement;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;

/**
 * Created by a.klimovich on 30.09.2016.
 */
public class RouteFollower {
    private Route route;
    MapPoint position;
    MapPoint nextPoint;
    double speed;
    RouteFollower routeFollower;
    int lane;

    boolean isOnEdge;
    NodeRoadElement curNode;
    EdgeRoadElement curEdge;
    NodeRoadElement nextNode;
    EdgeRoadElement nextEdge;

    public RouteFollower(Route route, int initialLane) {
        this.route = route;
        NodeRoadElement source = route.getSource();
        EdgeRoadElement edge = route.getNextEdge(source);
        DirectedRoad currentRoad = edge.getDirectedRoadByStartNode(source);
        BezierCurve currentLane = currentRoad.getSegments()[0].getLane(initialLane).getCurve();
        position = currentLane.getPoints()[0];
        nextPoint = currentLane.getPoints()[3];
        isOnEdge = true;
        curNode = null;
        curEdge = edge;
        nextEdge = null;
        nextNode = route.getNextNode(edge);
        lane = initialLane;
    }

    public void updatePosition(double speed, double timeDelta) {
        while (timeDelta > 0) {
            double distance = position.calculateDistance(nextPoint);
            if (distance < timeDelta * speed) {
                position = nextPoint;

                if (isOnEdge) {
                    isOnEdge = false;
                    curEdge = null;
                    curNode = nextNode;
                    nextEdge = route.getNextEdge(curNode);
                    nextNode = null;
                    if (nextEdge == null) {
                        break;
                    }

                    DirectedRoad road = nextEdge.getDirectedRoadByStartNode(curNode);
                    lane = Math.min(lane, road.getNumLanes() - 1);
                    nextPoint = road.getSegments()[0].getLane(lane).getCurve().getPoints()[0];
                }
                else {
                    DirectedRoad road = nextEdge.getDirectedRoadByStartNode(curNode);

                    isOnEdge = true;
                    curEdge = nextEdge;
                    curNode = null;
                    nextEdge = null;
                    nextNode = route.getNextNode(curEdge);

                    lane = Math.min(lane, road.getNumLanes() - 1);
                    nextPoint = road.getSegments()[0].getLane(lane).getCurve().getPoints()[3];
                }

                timeDelta -= distance / speed;
            } else {
                Vector velocity = new Vector(position, nextPoint).setLength(speed);
                position = velocity.clone().multiply(timeDelta).addToPoint(position);
                timeDelta = 0;
            }
        }
    }

    public MapPoint getPosition() {
        return position;
    }

    public Vector getDirection() {
        return new Vector(position, nextPoint);
    }

    public boolean reachedDestination() {
        return (!isOnEdge && route.getNextEdge(curNode) == null);
    }
}
