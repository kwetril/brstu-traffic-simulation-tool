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

    public BaseVehicleDriver(MovingVehicle vehicle) {
        this.vehicle = vehicle;
        VehicleTechnicalParameters techParams = vehicle.getVehicleInfo().getTechnicalParameters();
        maxAcceleration = techParams.getMaxAccelerationRate();
        maxDeceleration = techParams.getMaxDecelerationRate();
    }

    private double getPrefferedDistance(double convergenceSpeed) {
        double timeToStop = vehicle.getSpeed() / maxDeceleration;
        return convergenceSpeed * timeToStop + 0.3 * vehicle.getSpeed() + 8;
    }

    protected double getPrefferedAcceleration() {
        if (vehicle.getSpeed() < 10) {
            return maxAcceleration;
        } else {
            return maxAcceleration * 0.5;
        }
    }

    protected double getDistanceToStop() {
        double timeToStop = vehicle.getSpeed() / maxDeceleration;
        return maxDeceleration * timeToStop * timeToStop / 2;
    }

    public boolean seeCarsInFront() {
        return carsInfront;
    }

    protected boolean checkCarsInfront(SimulationState state) {
        Vector direction = routeState.getDirection();
        MapPoint position = routeState.getPosition();
        carsInfront = true;
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
        carsInfront = false;
        vehicle.setAcceletation(maxAcceleration);
        return false;
    }

    protected boolean checkIntersectionClosed(IntersectionState intersectionState) {
        if (!routeState.isBeforeIntersection() || intersectionState == null) {
            vehicle.setAcceletation(getPrefferedAcceleration());
            return false;
        }
        double approxDist = routeState.getCurrentRoad().getEndPoint().distanceTo(routeState.getPosition());
        if (approxDist > getDistanceToStop() + 15) {
            vehicle.setAcceletation(getPrefferedAcceleration());
            return false;
        }

        if (approxDist < getDistanceToStop() + 5) {
            //run as can't stop before
            return false;
        }

        DirectedRoad currentRoad = routeState.getCurrentRoad();
        int currentLane = routeState.getLane();
        DirectedRoad nextRoad = routeState.getNextRoad();
        int nextLane = Math.min(currentLane, nextRoad.getNumLanes());
        if (intersectionState.isOpened(currentRoad, currentLane, nextRoad, nextLane)) {
            vehicle.setAcceletation(getPrefferedAcceleration());
            return false;
        }

        vehicle.setAcceletation(-maxDeceleration);
        return true;
    }
}
