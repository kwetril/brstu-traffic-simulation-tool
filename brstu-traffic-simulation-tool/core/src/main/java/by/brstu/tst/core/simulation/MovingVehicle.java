package by.brstu.tst.core.simulation;

import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.simulation.driving.VehicleDriver;
import by.brstu.tst.core.simulation.routing.Route;
import by.brstu.tst.core.simulation.routing.RouteState;
import by.brstu.tst.core.simulation.routing.info.RouteStateInfo;
import by.brstu.tst.core.simulation.routing.state.ChangeLaneType;
import by.brstu.tst.core.vehicle.Vehicle;

/**
 * Created by a.klimovich on 10.09.2016.
 */
public class MovingVehicle {
    private Vehicle vehicle;
    double speed;
    RouteState routeState;
    VehicleDriver driver;

    public MovingVehicle(Vehicle vehicle, Route route, double initialSpeed, int initialLane) {
        this.vehicle = vehicle;
        routeState = new RouteState(route, initialLane);
        speed = initialSpeed;
        driver = new VehicleDriver(this);
    }

    public void accept(IVehicleVisitor visitor) {
        visitor.visit(this);
    }

    public RouteStateInfo getRouteStateInfo() {
        return routeState.getStateInfo();
    }

    public void updatePosition(float timeDelta) {
        routeState.updatePosition(speed * timeDelta);
    }

    public Vehicle getVehicleInfo() {
        return vehicle;
    }

    @Override
    public String toString() {
        MapPoint position = routeState.getStateInfo().getPosition();
        return String.format("[Vehicle %s: (%.2f, %.2f); %s mps]", vehicle.getIdentifier(),
                position.getX(), position.getY(), speed);
    }

    public void updateState(SimulationState state) {
        driver.updateVehicleState(state);
    }

    public void changeLane(ChangeLaneType changeLaneType) {
        routeState.changeLane(changeLaneType, 50);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
}
