package by.brstu.tst.core.simulation.driving.adaptive;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.driving.BaseVehicleDriver;
import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;
import by.brstu.tst.core.simulation.messaging.adaptive.AdaptiveNewVehicleNotification;
import by.brstu.tst.core.simulation.messaging.cyclic.BroadcastIntersectionStateMessage;
import by.brstu.tst.core.simulation.messaging.cyclic.GetSuitableLanesRequestMessage;
import by.brstu.tst.core.simulation.messaging.cyclic.GetSuitableLanesResponseMessage;
import by.brstu.tst.core.simulation.routing.info.RouteStateInfo;
import by.brstu.tst.core.simulation.routing.state.ChangeLaneType;
import by.brstu.tst.core.vehicle.VehicleTechnicalParameters;
import com.google.common.collect.Iterables;

import java.util.List;


/**
 * Created by a.klimovich on 02.10.2016.
 */
public class AdaptiveVehicleDriver extends BaseVehicleDriver {
    private double maxAcceleration;
    private double maxDeceleration;
    private RouteStateInfo routeState;
    private IntersectionState intersectionState;
    private List<Integer> suitableLanes;
    private boolean carsInfront;

    boolean messageSent;

    public AdaptiveVehicleDriver(MovingVehicle vehicle) {
        super(vehicle);
        VehicleTechnicalParameters techParams = vehicle.getVehicleInfo().getTechnicalParameters();
        maxAcceleration = techParams.getMaxAccelerationRate();
        maxDeceleration = techParams.getMaxDecelerationRate();
        intersectionState = null;
        suitableLanes = null;
        messageSent = false;
    }

    @Override
    public void updateInnerState(SimulationState simulationState, MessagingQueue messagingQueue) {
        routeState = vehicle.getRouteStateInfo();
        if (!routeState.isBeforeIntersection()) {
            intersectionState = null;
            suitableLanes = null;
            messageSent = false;
        } else {
            if (!messageSent
                    && routeState.getPosition().distanceTo(routeState.getNextIntersection().getBasePoint()) < 300) {
                messagingQueue.addMessage(new AdaptiveNewVehicleNotification(vehicle.getVehicleInfo().getIdentifier(),
                        routeState.getCurrentRoad().getName(), routeState.getNextIntersection().getName()));
                messageSent = true;
            }
        }
        processMessages(messagingQueue.getCurrentMessages());
        updateVehicleState(simulationState);
        sendMessages(messagingQueue);
    }

    @Override
    public boolean seeCarsInFront() {
        return carsInfront;
    }

    private void processMessages(Iterable<ControlMessage> messages) {
        Iterable<ControlMessage> messagesToProcess = Iterables.filter(messages,
                message -> message.isBroadcast() || message.getReceiver().equals(vehicle.getVehicleInfo().getIdentifier()));
        for (ControlMessage message : messagesToProcess) {
            switch (message.getType()) {
                case CYCLIC_INTERSECTION_STATE:
                    BroadcastIntersectionStateMessage m1 = (BroadcastIntersectionStateMessage) message;
                    if (routeState.isBeforeIntersection()
                            && routeState.getNextIntersection().getName().equals(m1.getSender())) {
                        intersectionState = m1.getIntersectionState();
                    }
                    break;
                case CYCLIC_GET_SUITABLE_LANES_RESPONSE:
                    GetSuitableLanesResponseMessage m2 = (GetSuitableLanesResponseMessage) message;
                    suitableLanes = m2.getSuitableLanes();
                    break;
                default:
                    throw new RuntimeException("Unsupported message type");
            }
        }
    }

    private void updateVehicleState(SimulationState simulationState) {
        vehicle.setAcceletation(getPrefferedAcceleration());

        carsInfront = checkCarsInfront(simulationState);
        if (carsInfront) {
            return;
        }
        boolean intersectionClosed = checkIntersectionClosed(intersectionState);
        if (intersectionClosed) {
            return;
        }
        if (vehicle.getSpeed() > 10) {
            checkLaneChanging();
        }
    }

    private void sendMessages(MessagingQueue messagingQueue) {
        if (routeState.isBeforeIntersection() && suitableLanes == null) {
            messagingQueue.addMessage(new GetSuitableLanesRequestMessage(
                    vehicle.getVehicleInfo().getIdentifier(),
                    routeState.getNextIntersection().getName(),
                    routeState.getCurrentRoad(),
                    routeState.getNextRoad()));
        }
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

    private void checkLaneChanging() {
        if (routeState.isOnRoad()
                && routeState.isBeforeIntersection()
                && !routeState.isChangingLane()
                && suitableLanes != null
                && !suitableLanes.contains(routeState.getLane())) {
            int lane = routeState.getLane();
            suitableLanes.sort((l1, l2) -> Math.abs(l1 - lane) - Math.abs(l2 - lane));
            int laneTo = suitableLanes.get(0);
            if (laneTo > lane) {
                vehicle.changeLane(ChangeLaneType.RIGHT);
            } else {
                vehicle.changeLane(ChangeLaneType.LEFT);
            }
        }
    }

    private boolean checkCarsInfront(SimulationState state) {
        Vector direction = routeState.getDirection();
        MapPoint position = routeState.getPosition();
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
        vehicle.setAcceletation(maxAcceleration);
        return false;
    }

    private boolean checkIntersectionClosed(IntersectionState intersectionState) {
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
