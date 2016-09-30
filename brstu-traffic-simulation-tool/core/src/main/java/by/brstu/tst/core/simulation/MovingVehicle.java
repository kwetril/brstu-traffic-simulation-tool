package by.brstu.tst.core.simulation;

import by.brstu.tst.core.map.elements.*;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.simulation.flows.Route;
import by.brstu.tst.core.vehicle.Vehicle;

/**
 * Created by a.klimovich on 10.09.2016.
 */
public class MovingVehicle {
    private Vehicle vehicle;
    MapPoint position;
    MapPoint nextPoint;
    Vector velocity;
    double speed;
    Route route;
    int lane;

    boolean isOnEdge;
    NodeRoadElement curNode;
    EdgeRoadElement curEdge;
    NodeRoadElement nextNode;
    EdgeRoadElement nextEdge;

    public MovingVehicle(Vehicle vehicle, Route route, double initialSpeed, int initialLane) {
        this.vehicle = vehicle;
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
        speed = initialSpeed;
        lane = initialLane;
    }

    public void accept(IVehicleVisitor visitor) {
        visitor.visit(this);
    }

    public MapPoint updatePosition(float timeDelta) {
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
                velocity = new Vector(position, nextPoint).setLength(speed);
                position = velocity.clone().multiply(timeDelta).addToPoint(position);
                timeDelta = 0;
            }
        }
        return position;
    }

    public MapPoint getPosition() {
        return position;
    }

    public boolean reachedDestination() {
        return (!isOnEdge && route.getNextEdge(curNode) == null);
    }

    public Vehicle getVehicleInfo() {
        return vehicle;
    }

    public Vector getVelocity() {
        return velocity;
    }

    @Override
    public String toString() {
        return String.format("[Vehicle %s: (%.2f, %.2f); %s mps]", vehicle.getIdentifier(),
                position.getX(), position.getY(), velocity.getLength());
    }
}
