package by.brstu.tst.core.simulation.driving.autonomous;

import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.driving.BaseVehicleDriver;
import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;
import by.brstu.tst.core.simulation.messaging.autonomous.*;
import by.brstu.tst.core.simulation.routing.state.ChangeLaneType;
import com.google.common.collect.Iterables;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class AutonomousVehicleDriver extends BaseVehicleDriver {
    private IntersectionState intersectionState;
    private Intersection currentIntersection; //is set when vehicle is on intersection and unsed when it's passed
    private double lastUpdateTime = -1;
    private double waitingTime = 0;
    private final double speedLimitToCountWaiting = 3;

    private int choosenLane = -2;

    public AutonomousVehicleDriver(MovingVehicle vehicle) {
        super(vehicle);
        intersectionState = null;
        currentIntersection = null;
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
                case AUTONOMOUS_INTERSECTION_STATE:
                    if (!routeState.isBeforeIntersection()
                            || !routeState.getNextIntersection().getName().equals(message.getSender())) {
                        continue;
                    }
                    AutonomousIntersectionStateMessage stateMessage = (AutonomousIntersectionStateMessage) message;
                    intersectionState = stateMessage.getState();
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
        if (checkCarsInfront(simulationState)) {
            return;
        }
        if (checkIntersectionClosed(intersectionState)) {
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
        } else {
            if (routeState.isOnRoad()) {
                //intersection passed
                messageQueue.addMessage(new IntersectionPassedNotification(vehicle.getVehicleInfo().getIdentifier(),
                        currentIntersection.getName()));
                currentIntersection = null;
                choosenLane = -2;
            }
        }
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
