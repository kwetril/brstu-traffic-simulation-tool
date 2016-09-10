package by.brstu.tst.core.simulation;

import by.brstu.tst.core.map.elements.RoadLane;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.map.utils.MapUtils;
import by.brstu.tst.core.simulation.flows.Route;
import by.brstu.tst.core.vehicle.Vehicle;

/**
 * Created by a.klimovich on 10.09.2016.
 */
public class MovingVehicle {
    private Vehicle vehicle;
    private RoadLane currentLane;
    MapPoint position;
    Vector direction;
    Vector velocity;
    Route route;

    public MovingVehicle(Vehicle vehicle, Route route) {
        this.vehicle = vehicle;
        this.route = route;
        position = route.getSource().getBasePoint();
        MapPoint destination = route.getDestination().getBasePoint();
        velocity = new Vector(position, destination).setLength(10);
    }

    public void accept(IVehicleVisitor visitor) {
        visitor.visit(this);
    }

    public MapPoint updatePosition(float timeDelta) {
        MapPoint destination = route.getDestination().getBasePoint();
        velocity = new Vector(position, destination).setLength(10);
        position = velocity.clone().multiply(timeDelta).addToPoint(position);
        return position;
    }

    public MapPoint getPosition() {
        return position;
    }

    public boolean reachedDestination() {
        MapPoint destination = route.getDestination().getBasePoint();
        if (new Vector(position, destination).getLength() < 1) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("[Vehicle %s: (%s, %s); %s mps]", vehicle.getIdentifier(),
                position.getX(), position.getY(), velocity.getLength());
    }
}
