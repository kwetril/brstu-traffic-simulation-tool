package by.brstu.tst.core.simulation.driving;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.messaging.IMessagingAgent;
import by.brstu.tst.core.simulation.routing.info.RouteStateInfo;
import by.brstu.tst.core.vehicle.VehicleTechnicalParameters;

/**
 * Created by a.klimovich on 17.10.2016.
 */
public abstract class BaseVehicleDriver implements IMessagingAgent {
    protected MovingVehicle vehicle;
    protected double maxAcceleration;
    protected double maxDeceleration;
    protected RouteStateInfo routeState;
    private boolean carsInfront;
    private IntelligentDriverModel accelerationModel;

    public BaseVehicleDriver(MovingVehicle vehicle) {
        this.vehicle = vehicle;
        VehicleTechnicalParameters techParams = vehicle.getVehicleInfo().getTechnicalParameters();
        maxAcceleration = techParams.getMaxAccelerationRate();
        maxDeceleration = techParams.getMaxDecelerationRate();
        accelerationModel = new IntelligentDriverModel(maxAcceleration, maxDeceleration, 1.3, 4.0, 2);
    }

    private double calculateBamperDistance(MovingVehicle anotherVehicle) {
        double centerDistance = vehicle.getRouteStateInfo().getPosition().distanceTo(anotherVehicle.getRouteStateInfo().getPosition());
        return  Math.max(0, centerDistance - vehicle.getVehicleInfo().getTechnicalParameters().getWidth() / 2
                - anotherVehicle.getVehicleInfo().getTechnicalParameters().getWidth() / 2);
    }

    private MovingVehicle findLeadingVehicle(Iterable<MovingVehicle> vehicles) {
        Vector direction = routeState.getDirection();
        MapPoint position = routeState.getPosition();
        MovingVehicle result = null;
        double resultDistance = 10000;
        for (MovingVehicle otherVehicle: vehicles) {
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
            if ((routeState.isOnRoad() && otherStateInfo.isOnRoad()
                    && otherStateInfo.getCurrentRoad().getName().equals(routeState.getCurrentRoad().getName())
                    && routeState.getLane() == otherStateInfo.getLane())
                    || (!routeState.isOnRoad() && !otherStateInfo.isOnRoad()
                    && routeState.getNextRoad().getName().equals(otherStateInfo.getNextRoad().getName())
                    && routeState.getLaneAfterNode() == otherStateInfo.getLaneAfterNode())
                    || (routeState.isOnRoad() && !otherStateInfo.isOnRoad()
                    && routeState.getLane() == otherStateInfo.getLaneAfterNode())
                    || (!routeState.isOnRoad() && otherStateInfo.isOnRoad()
                    && routeState.getNextRoad().getName().equals(otherStateInfo.getCurrentRoad().getName())
                    && routeState.getLaneAfterNode() == otherStateInfo.getLane())) {
                if (convergenceSpeed > 0 && fromToVector.scalarMultiply(direction) > 0
                        && (result == null || distance < resultDistance)) {
                    result = otherVehicle;
                    resultDistance = distance;
                } else if (convergenceSpeed <= 0) {
                    double d1 = direction.clone().setLength(distance).addToPoint(position).distanceTo(otherPosition);
                    double d2 = otherDirection.clone().setLength(distance).addToPoint(otherPosition).distanceTo(position);
                    if (d1 < d2 && (result == null || distance < resultDistance)) {
                        result = otherVehicle;
                        resultDistance = distance;
                    }
                }
            }
        }
        return result;
    }

    protected double getPrefferedAcceleration() {
        return maxAcceleration;
    }

    public boolean seeCarsInFront() {
        return carsInfront;
    }

    protected boolean checkCarsInfront(SimulationState state) {
        Vector direction = routeState.getDirection();
        MapPoint position = routeState.getPosition();
        MovingVehicle leadingVehicle = findLeadingVehicle(state.getVehicles());
        if (leadingVehicle == null) {
            carsInfront = false;
            return carsInfront;
        }
        carsInfront = true;
        RouteStateInfo otherStateInfo = leadingVehicle.getRouteStateInfo();
        Vector otherDirection = otherStateInfo.getDirection();
        MapPoint otherPosition = otherStateInfo.getPosition();

        Vector fromToVector = new Vector(position, otherPosition);

        Vector relationalSpeed = direction.clone().setLength(vehicle.getSpeed())
                .add(otherDirection.clone().setLength(leadingVehicle.getSpeed()).multiply(-1));
        double convergenceSpeed = relationalSpeed.scalarMultiply(fromToVector.setLength(1.0));
        vehicle.setAcceletation(accelerationModel.getAcceleration(vehicle.getSpeed(), convergenceSpeed,
                calculateBamperDistance(leadingVehicle), 20));
        return carsInfront;
    }

    protected boolean checkIntersectionClosed(IntersectionState intersectionState) {
        if (!routeState.isBeforeIntersection() || intersectionState == null) {
            return false;
        }
        double approxDist = routeState.getCurrentRoad().getEndPoint().distanceTo(routeState.getPosition());
        if (approxDist < 5) {
            approxDist = 10000;
        } else if (approxDist < 10) {
            approxDist = 0;
        }

        DirectedRoad currentRoad = routeState.getCurrentRoad();
        int currentLane = routeState.getLane();
        DirectedRoad nextRoad = routeState.getNextRoad();
        int nextLane = Math.min(currentLane, nextRoad.getNumLanes());
        if (intersectionState.isOpened(currentRoad, currentLane, nextRoad, nextLane)) {
            return false;
        }

        vehicle.setAcceletation(accelerationModel.getAcceleration(vehicle.getSpeed(), vehicle.getSpeed(), approxDist, 20));
        return true;
    }
}
