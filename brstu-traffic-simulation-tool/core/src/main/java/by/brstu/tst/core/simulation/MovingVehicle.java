package by.brstu.tst.core.simulation;

import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.routing.Route;
import by.brstu.tst.core.simulation.routing.RouteFollower;
import by.brstu.tst.core.vehicle.Vehicle;

import java.util.List;

/**
 * Created by a.klimovich on 10.09.2016.
 */
public class MovingVehicle {
    private Vehicle vehicle;
    double speed;
    RouteFollower routeFollower;

    public MovingVehicle(Vehicle vehicle, Route route, double initialSpeed, int initialLane) {
        this.vehicle = vehicle;
        routeFollower = new RouteFollower(route, initialLane);
        speed = initialSpeed;
    }

    public void accept(IVehicleVisitor visitor) {
        visitor.visit(this);
    }

    private void receiveControlMessages(List<ControlMessage> messages) {

    }

    private void sendMessages() {

    }

    public void updatePosition(float timeDelta) {
        routeFollower.updatePosition(speed * timeDelta);
    }

    public MapPoint getPosition() {
        return routeFollower.getPosition();
    }

    public boolean reachedDestination() {
        return routeFollower.reachedDestination();
    }

    public Vehicle getVehicleInfo() {
        return vehicle;
    }

    public Vector getDirection() {
        return routeFollower.getDirection();
    }

    @Override
    public String toString() {
        return String.format("[Vehicle %s: (%.2f, %.2f); %s mps]", vehicle.getIdentifier(),
                getPosition().getX(), getPosition().getY(), speed);
    }
}
