package by.brstu.tst.core.simulation.driving.autonomous;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.driving.BaseVehicleDriver;
import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;
import by.brstu.tst.core.simulation.messaging.autonomous.*;
import by.brstu.tst.core.simulation.routing.info.RouteStateInfo;
import by.brstu.tst.core.simulation.routing.state.ChangeLaneType;
import by.brstu.tst.core.vehicle.VehicleTechnicalParameters;
import com.google.common.collect.Iterables;

import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class AutonomousVehicleDriver extends BaseVehicleDriver {
    private double maxAcceleration;
    private double maxDeceleration;
    private RouteStateInfo routeState;
    private IntersectionState intersectionState;
    private Intersection currentIntersection; //is set when vehicle is on intersection and unsed when it's passed
    private double lastUpdateTime = -1;
    private double waitingTime = 0;
    private final double speedLimitToCountWaiting = 3;
    private boolean carsInFront;

    private int choosenLane = -2;

    public AutonomousVehicleDriver(MovingVehicle vehicle) {
        super(vehicle);
        VehicleTechnicalParameters techParams = vehicle.getVehicleInfo().getTechnicalParameters();
        maxAcceleration = techParams.getMaxAccelerationRate();
        maxDeceleration = techParams.getMaxDecelerationRate();
        intersectionState = null;
        currentIntersection = null;
    }

    @Override
    public boolean seeCarsInFront() {
        return carsInFront;
    }

    @Override
    public void updateInnerState(SimulationState simulationState, MessagingQueue messagingQueue) {
        routeState = vehicle.getRouteStateInfo();
        processMessages(messagingQueue);
        updateVehicleState(simulationState);
        sendNotifications(messagingQueue);
    }

    private void processMessages(MessagingQueue messageQueue) {
        Iterable<ControlMessage> messagesToProcess = Iterables.filter(messageQueue.getCurrentMessages(),
                message -> message.isBroadcast() || message.getReceiver().equals(vehicle.getVehicleInfo().getIdentifier()));
        for (ControlMessage message : messagesToProcess) {
            switch (message.getType()) {
                case AUTONOMOUS_REQUEST_DIRECTIONS:
                    if (!routeState.isBeforeIntersection()
                            || !routeState.getNextIntersection().getName().equals(message.getSender())) {
                        continue;
                    }
                    RoadConnectorDescription roadConnector = new RoadConnectorDescription(
                            routeState.getCurrentRoad(),
                            routeState.getLane(),
                            routeState.getNextRoad(),
                            routeState.getLane()
                    );
                    messageQueue.addMessage(new ResponseVehicleDirection(vehicle.getVehicleInfo().getIdentifier(),
                            message.getSender(), routeState.getPosition(), roadConnector, waitingTime));
                    break;
                case AUTONMOUS_INTERSECTION_COMMAND:
                    break;
                case AUTONOMOUS_INTERSECTION_STATE:
                    if (!routeState.isBeforeIntersection()
                            || !routeState.getNextIntersection().getName().equals(message.getSender())) {
                        continue;
                    }
                    intersectionState = ((AutonomousIntersectionStateMessage) message).getState();
                    break;
                case AUTONOMOUS_RESPONSE_PREFFERED_LANES:
                    if (choosenLane == -1) {
                        chooseLane((ResponsePrefferedLanes) message);
                    }
                    break;
                default:
                    throw new RuntimeException("Unsupported message type");
            }
        }
    }

    private void chooseLane(ResponsePrefferedLanes message) {
        HashMap<Integer, Double> laneToPriority = message.getLaneToPriority();
        Random rand = new Random();
        double randomValue = rand.nextDouble();
        int i = 0;
        for (Map.Entry<Integer, Double> laneWithPriority : laneToPriority.entrySet()) {
            if (randomValue < laneWithPriority.getValue() || i + 1 >= laneToPriority.size()) {
                choosenLane = laneWithPriority.getKey();
                break;
            }
            randomValue -= laneWithPriority.getValue();
            i++;
        }
    }

    private void updateVehicleState(SimulationState simulationState) {
        vehicle.setAcceletation(getPrefferedAcceleration());

        carsInFront = checkCarsInfront(simulationState);
        if (carsInFront) {
            return;
        }
        boolean intersectionClosed = checkIntersectionClosed(intersectionState);
        if (intersectionClosed) {
            return;
        }
        if (vehicle.getSpeed() > 10) {
            checkLaneChanging();
        }

        //calculate waiting time
        if (lastUpdateTime >= 0) {
            if (vehicle.getSpeed() < speedLimitToCountWaiting) {
                waitingTime += simulationState.getSimulationTime() - lastUpdateTime;
            }
        }
        lastUpdateTime = simulationState.getSimulationTime();
    }

    private void sendNotifications(MessagingQueue messageQueue) {
        if (routeState.isBeforeIntersection() && choosenLane == -2) {
            messageQueue.addMessage(new RequestPrefferedLanes(vehicle.getVehicleInfo().getIdentifier(),
                    routeState.getNextIntersection().getName(),
                    routeState.getCurrentRoad(), routeState.getNextRoad()));
            choosenLane = -1;
        }
        if (currentIntersection == null) {
            if (!routeState.isOnRoad() && routeState.getCurrentNode() instanceof Intersection) {
                currentIntersection = (Intersection) routeState.getCurrentNode();
                //enter intersection
            }
        }
        else {
            if (routeState.isOnRoad()) {
                //intersection passed
                messageQueue.addMessage(new IntersectionPassedNotification(vehicle.getVehicleInfo().getIdentifier(),
                        currentIntersection.getName()));
                currentIntersection = null;
                choosenLane = -2;
            }
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

    private void checkLaneChanging() {
        if (routeState.isOnRoad()
                && routeState.isBeforeIntersection()
                && !routeState.isChangingLane()
                && choosenLane >= 0
                && choosenLane != routeState.getLane()) {
            int lane = routeState.getLane();
            if (choosenLane > lane) {
                vehicle.changeLane(ChangeLaneType.RIGHT);
            } else {
                vehicle.changeLane(ChangeLaneType.LEFT);
            }
        }
    }
}
