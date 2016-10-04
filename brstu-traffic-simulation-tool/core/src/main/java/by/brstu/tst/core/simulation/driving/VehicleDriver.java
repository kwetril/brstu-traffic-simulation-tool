package by.brstu.tst.core.simulation.driving;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.control.IntersectionController;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.routing.RouteState;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class VehicleDriver {
    private MovingVehicle vehicle;

    public VehicleDriver(MovingVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void updateVehicleState(SimulationState state) {
        if (vehicle.getSpeed() < 1) {
            vehicle.setSpeed(1);
        }
        boolean carsInfront = checkCarsInfront(state);
        if (carsInfront) {
            return;
        }
        boolean intersectionClosed = checkIntersectionClosed(state);
        if (intersectionClosed) {
            return;
        }
    }

    private boolean checkCarsInfront(SimulationState state) {
        RouteState routeState = vehicle.getRouteState();
        String curRoadName = null;
        if (routeState.isOnRoad()) {
            curRoadName = routeState.getCurrentRoad().getName();
        }
        int curLane = routeState.getCurrentLane();
        Vector direction = routeState.getDirection();
        MapPoint position = routeState.getPosition();
        for (MovingVehicle otherVehicle : state.getVehicles()) {
            if (otherVehicle.getVehicleInfo().getIdentifier().equals(vehicle.getVehicleInfo().getIdentifier())) {
                continue;
            }
            RouteState otherRouteState = otherVehicle.getRouteState();
            Vector otherDirection = otherRouteState.getDirection();
            MapPoint otherPosition = otherRouteState.getPosition();

            Vector fromToVector = new Vector(position, otherPosition);
            double distance = fromToVector.getLength();

            Vector relationalSpeed = direction.clone().setLength(vehicle.getSpeed())
                    .add(otherDirection.clone().setLength(otherVehicle.getSpeed()).multiply(-1));
            if (relationalSpeed.scalarMultiply(direction) < 0) {
                continue;
            }
            double convergenceSpeed = relationalSpeed.scalarMultiply(fromToVector.setLength(1.0));

            if (convergenceSpeed > 0 && distance < convergenceSpeed * 1.0f + 8.0) {
                if (routeState.isOnRoad() && otherRouteState.isOnRoad()
                        && otherRouteState.getCurrentRoad().getName().equals(curRoadName)
                        && curLane == otherRouteState.getCurrentLane()) {
                    vehicle.setSpeed(0);
                    return true;
                }
            }
        }
        vehicle.setSpeed(20);
        return false;
    }

    private boolean checkIntersectionClosed(SimulationState state) {
        RouteState routeState = vehicle.getRouteState();
        if (!routeState.isBeforeIntersection()) {
            vehicle.setSpeed(20);
            return false;
        }
        double approxDist = routeState.getCurrentRoad().getEndPoint().distanceTo(routeState.getPosition());
        if (approxDist > 10) {
            vehicle.setSpeed(20);
            return false;
        }

        Intersection intersection = routeState.getNextIntersection();
        IntersectionController controller = state.getController(intersection);
        IntersectionState intersectionState = controller.getStateByTime(state.getSimulationTime());
        DirectedRoad currentRoad = routeState.getCurrentRoad();
        int currentLane = routeState.getCurrentLane();
        DirectedRoad nextRoad = routeState.getNextRoad();
        int nextLane = Math.min(currentLane, nextRoad.getNumLanes());
        if (intersectionState.isOpened(currentRoad, currentLane, nextRoad, nextLane)) {
            vehicle.setSpeed(20);
            return false;
        }

        vehicle.setSpeed(0);
        return true;
    }
}
