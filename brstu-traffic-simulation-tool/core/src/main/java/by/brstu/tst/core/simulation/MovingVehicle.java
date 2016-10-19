package by.brstu.tst.core.simulation;

import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.simulation.driving.BaseVehicleDriver;
import by.brstu.tst.core.simulation.driving.IDriverFactory;
import by.brstu.tst.core.simulation.driving.cyclic.CyclicVehicleDriver;
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
    private double speed;
    private double acceletation;
    private RouteState routeState;
    private BaseVehicleDriver driver;


    public MovingVehicle(Vehicle vehicle, IDriverFactory driverFactory, Route route,
                         double initialSpeed, int initialLane) {
        this.vehicle = vehicle;
        routeState = new RouteState(route, initialLane);
        speed = initialSpeed;
        acceletation = 0;
        driver = driverFactory.createDriver(this);
    }

    public void accept(IVehicleVisitor visitor) {
        visitor.visit(this);
    }

    public RouteStateInfo getRouteStateInfo() {
        return routeState.getStateInfo();
    }

    public void updatePosition(float timeDelta) {
        double deltaSpeed = acceletation * timeDelta;
        double deltaDistance = speed * timeDelta + deltaSpeed * timeDelta / 2;
        routeState.updatePosition(deltaDistance);
        speed = Math.max(0, Math.min(20, speed + deltaSpeed));
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

    public BaseVehicleDriver getDriver() {
        return driver;
    }

    public void changeLane(ChangeLaneType changeLaneType) {
        routeState.changeLane(changeLaneType, 50);
    }

    public void setAcceletation(double acceletation) {
        this.acceletation = acceletation;
    }

    public double getSpeed() {
        return speed;
    }
}
