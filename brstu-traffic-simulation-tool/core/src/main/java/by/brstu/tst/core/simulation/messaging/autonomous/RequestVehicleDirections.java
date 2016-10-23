package by.brstu.tst.core.simulation.messaging.autonomous;

import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.ControlMessageType;
import org.omg.PortableServer.REQUEST_PROCESSING_POLICY_ID;

import static by.brstu.tst.core.simulation.messaging.ControlMessageType.AUTONOMOUS_REQUEST_DIRECTIONS;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class RequestVehicleDirections extends ControlMessage {
    public RequestVehicleDirections(String sender) {
        super(AUTONOMOUS_REQUEST_DIRECTIONS, sender, null);
    }
}
