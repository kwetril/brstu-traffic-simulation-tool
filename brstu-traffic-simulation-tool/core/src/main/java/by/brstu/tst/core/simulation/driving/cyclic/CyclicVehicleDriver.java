package by.brstu.tst.core.simulation.driving.cyclic;

import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.driving.BaseVehicleDriver;
import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;
import by.brstu.tst.core.simulation.messaging.BroadcastIntersectionStateMessage;
import by.brstu.tst.core.simulation.messaging.cyclic.GetSuitableLanesRequestMessage;
import by.brstu.tst.core.simulation.messaging.cyclic.GetSuitableLanesResponseMessage;
import by.brstu.tst.core.simulation.routing.state.ChangeLaneType;
import com.google.common.collect.Iterables;

import java.util.List;


/**
 * Created by a.klimovich on 02.10.2016.
 */
public class CyclicVehicleDriver extends BaseVehicleDriver {
    private IntersectionState intersectionState;
    private List<Integer> suitableLanes;

    public CyclicVehicleDriver(MovingVehicle vehicle) {
        super(vehicle);
        intersectionState = null;
        suitableLanes = null;
    }

    @Override
    public void updateInnerState(SimulationState simulationState, MessagingQueue messagingQueue) {
        routeState = vehicle.getRouteStateInfo();
        if (!routeState.isBeforeIntersection()) {
            intersectionState = null;
            suitableLanes = null;
        }
        processMessages(messagingQueue.getCurrentMessages());
        updateVehicleState(simulationState);
        sendMessages(messagingQueue);
    }

    private void processMessages(Iterable<ControlMessage> messages) {
        Iterable<ControlMessage> messagesToProcess = Iterables.filter(messages,
                message -> message.isBroadcast() || message.getReceiver().equals(vehicle.getVehicleInfo().getIdentifier()));
        for (ControlMessage message : messagesToProcess) {
            switch (message.getType()) {
                case BROADCAST_INTERSECTION_STATE:
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
        if (checkCarsInfront(simulationState)) {
            return;
        }
        if (checkIntersectionClosed(intersectionState)) {
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
}

