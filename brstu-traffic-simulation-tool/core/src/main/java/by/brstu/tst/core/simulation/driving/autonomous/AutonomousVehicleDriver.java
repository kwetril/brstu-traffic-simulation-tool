package by.brstu.tst.core.simulation.driving.autonomous;

import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.driving.BaseVehicleDriver;
import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;
import by.brstu.tst.core.simulation.messaging.autonomous.ResponseVehicleDirection;
import by.brstu.tst.core.simulation.routing.info.RouteStateInfo;
import com.google.common.collect.Iterables;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class AutonomousVehicleDriver extends BaseVehicleDriver {
    private RouteStateInfo routeState;

    public AutonomousVehicleDriver(MovingVehicle vehicle) {
        super(vehicle);
    }

    @Override
    public boolean seeCarsInFront() {
        return false;
    }

    @Override
    public void updateInnerState(SimulationState simulationState, MessagingQueue messagingQueue) {
        routeState = vehicle.getRouteStateInfo();

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
                            message.getSender(), routeState.getPosition(), roadConnector));
                    break;
                case AUTONMOUS_INTERSECTION_COMMAND:
                    break;
                default:
                    throw new RuntimeException("Unsupported message type");
            }
        }
    }
}
