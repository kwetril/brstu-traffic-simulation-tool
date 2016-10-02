package by.brstu.tst.core.simulation.driving;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
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
        RouteState routeState = vehicle.getRouteState();
        if (!routeState.isBeforeIntersection()) {
            vehicle.setSpeed(20);
            return;
        }
        Intersection intersection = routeState.getNextIntersection();
        double approxDist = intersection.getBasePoint().distanceTo(routeState.getPosition());
        if (approxDist > 100) {
            vehicle.setSpeed(20);
            return;
        }

        IntersectionController controller = state.getController(intersection);
        IntersectionState intersectionState = controller.getStateByTime(state.getSimulationTime());
        DirectedRoad currentRoad = routeState.getCurrentRoad();
        int currentLane = routeState.getCurrentLane();
        DirectedRoad nextRoad = routeState.getNextRoad();
        int nextLane = Math.min(currentLane, nextRoad.getNumLanes());
        if (intersectionState.isOpened(currentRoad, currentLane, nextRoad, nextLane)) {
            vehicle.setSpeed(20);
            return;
        }

        vehicle.setSpeed(0);
    }
}
