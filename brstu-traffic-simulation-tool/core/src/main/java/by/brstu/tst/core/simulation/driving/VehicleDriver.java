package by.brstu.tst.core.simulation.driving;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.control.IntersectionController;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.routing.RouteState;
import by.brstu.tst.core.simulation.routing.info.RouteStateInfo;
import by.brstu.tst.core.simulation.routing.state.ChangeLaneType;
import by.brstu.tst.core.vehicle.VehicleTechnicalParameters;

import java.util.Random;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class VehicleDriver {
    private MovingVehicle vehicle;
    private Random rand = new Random();
    private double maxAcceleration;
    private double maxDeceleration;

    public VehicleDriver(MovingVehicle vehicle) {
        this.vehicle = vehicle;
        VehicleTechnicalParameters techParams = vehicle.getVehicleInfo().getTechnicalParameters();
        maxAcceleration = techParams.getMaxAccelerationRate();
        maxDeceleration = techParams.getMaxDecelerationRate();
    }

    private double getPrefferedAcceleration() {
        if (vehicle.getSpeed() < 10) {
            return maxAcceleration;
        } else {
            return maxAcceleration * 0.5;
        }
    }

    private double getPrefferedDistance(double convergenceSpeed) {
        double timeToStop = vehicle.getSpeed() / maxDeceleration;
        return convergenceSpeed * timeToStop + 0.3 * vehicle.getSpeed() + 8;
    }

    private double getDistanceToStop() {
        double timeToStop = vehicle.getSpeed() / maxDeceleration;
        return maxDeceleration * timeToStop * timeToStop / 2;
    }

    public void updateVehicleState(SimulationState state) {
        vehicle.setAcceletation(getPrefferedAcceleration());

        RouteStateInfo routeStateInfo = vehicle.getRouteStateInfo();
        boolean carsInfront = checkCarsInfront(state, routeStateInfo);
        if (carsInfront) {
            return;
        }
        boolean intersectionClosed = checkIntersectionClosed(state, routeStateInfo);
        if (intersectionClosed) {
            return;
        }

        if (vehicle.getSpeed() > 10) {
            checkLaneChanging(state, routeStateInfo);
        }
    }

    private void checkLaneChanging(SimulationState state, RouteStateInfo routeStateInfo) {
        if (routeStateInfo.isOnRoad() && routeStateInfo.isBeforeIntersection() && !routeStateInfo.isChangingLane()) {
            DirectedRoad currentRoad = routeStateInfo.getCurrentRoad();
            int lane = routeStateInfo.getLane();
            DirectedRoad nextRoad = routeStateInfo.getNextRoad();
            Intersection intersection = routeStateInfo.getNextIntersection();
            IntersectionController controller = state.getController(intersection);
            if (controller.connectorExist(currentRoad, lane, nextRoad)) {
                return;
            }
            int laneTo;
            for (int i = 0; i < currentRoad.getNumLanes(); i++) {
                laneTo = Math.min(lane + i, currentRoad.getNumLanes() - 1);
                if (controller.connectorExist(currentRoad, laneTo, nextRoad)) {
                    vehicle.changeLane(ChangeLaneType.RIGHT);
                    return;
                }
                laneTo = Math.max(lane - i, 0);
                if (controller.connectorExist(currentRoad, laneTo, nextRoad)) {
                    vehicle.changeLane(ChangeLaneType.LEFT);
                    return;
                }
            }
        }
    }

    private boolean checkCarsInfront(SimulationState state, RouteStateInfo routeStateInfo) {
        Vector direction = routeStateInfo.getDirection();
        MapPoint position = routeStateInfo.getPosition();
        for (MovingVehicle otherVehicle : state.getVehicles()) {
            if (otherVehicle.getVehicleInfo().getIdentifier().equals(vehicle.getVehicleInfo().getIdentifier())) {
                continue;
            }
            RouteStateInfo otherStateInfo = otherVehicle.getRouteStateInfo();
            if (otherStateInfo.reachedDestination()) {
                continue;
            }
            Vector otherDirection = otherStateInfo.getDirection();
            MapPoint otherPosition = otherStateInfo.getPosition();

            Vector fromToVector = new Vector(position, otherPosition);
            double distance = fromToVector.getLength();

            Vector relationalSpeed = direction.clone().setLength(vehicle.getSpeed())
                    .add(otherDirection.clone().setLength(otherVehicle.getSpeed()).multiply(-1));
            double convergenceSpeed = relationalSpeed.scalarMultiply(fromToVector.setLength(1.0));
            if ((routeStateInfo.isOnRoad() && otherStateInfo.isOnRoad()
                    && otherStateInfo.getCurrentRoad().getName().equals(routeStateInfo.getCurrentRoad().getName())
                    && routeStateInfo.getLane() == otherStateInfo.getLane())
                    || (!routeStateInfo.isOnRoad() && !otherStateInfo.isOnRoad()
                    && routeStateInfo.getNextRoad().getName().equals(otherStateInfo.getNextRoad().getName())
                    && routeStateInfo.getLaneAfterNode() == otherStateInfo.getLaneAfterNode())
                    || (routeStateInfo.isOnRoad() && !otherStateInfo.isOnRoad()
                    && routeStateInfo.getLane() == otherStateInfo.getLaneAfterNode())
                    || (!routeStateInfo.isOnRoad() && otherStateInfo.isOnRoad()
                    && routeStateInfo.getNextRoad().getName().equals(otherStateInfo.getCurrentRoad().getName())
                    && routeStateInfo.getLaneAfterNode() == otherStateInfo.getLane())) {
                if (convergenceSpeed > 0 && relationalSpeed.scalarMultiply(direction) > 0
                        && distance < getPrefferedDistance(convergenceSpeed)) {
                    vehicle.setAcceletation(-maxDeceleration);
                    return true;
                } else if (convergenceSpeed <= 0 && distance < getPrefferedDistance(0)) {
                    double d1 = direction.clone().setLength(distance).addToPoint(position).distanceTo(otherPosition);
                    double d2 = otherDirection.clone().setLength(distance).addToPoint(otherPosition).distanceTo(position);
                    if (d1 < d2) {
                        vehicle.setAcceletation(-maxDeceleration);
                        return true;
                    }
                }
            }
        }
        vehicle.setAcceletation(maxAcceleration);
        return false;
    }

    private boolean checkIntersectionClosed(SimulationState state, RouteStateInfo routeStateInfo) {
        if (!routeStateInfo.isBeforeIntersection()) {
            vehicle.setAcceletation(getPrefferedAcceleration());
            return false;
        }
        double approxDist = routeStateInfo.getCurrentRoad().getEndPoint().distanceTo(routeStateInfo.getPosition());
        if (approxDist > getDistanceToStop() + 15) {
            vehicle.setAcceletation(getPrefferedAcceleration());
            return false;
        }

        if (approxDist < getDistanceToStop() + 5) {
            //run as can't stop before
            return false;
        }

        Intersection intersection = routeStateInfo.getNextIntersection();
        IntersectionController controller = state.getController(intersection);
        IntersectionState intersectionState = controller.getStateByTime(state.getSimulationTime());
        DirectedRoad currentRoad = routeStateInfo.getCurrentRoad();
        int currentLane = routeStateInfo.getLane();
        DirectedRoad nextRoad = routeStateInfo.getNextRoad();
        int nextLane = Math.min(currentLane, nextRoad.getNumLanes());
        if (intersectionState.isOpened(currentRoad, currentLane, nextRoad, nextLane)) {
            vehicle.setAcceletation(getPrefferedAcceleration());
            return false;
        }

        vehicle.setAcceletation(-maxDeceleration);
        return true;
    }
}

