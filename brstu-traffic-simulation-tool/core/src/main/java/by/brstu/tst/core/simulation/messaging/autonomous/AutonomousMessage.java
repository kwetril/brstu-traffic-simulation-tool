package by.brstu.tst.core.simulation.messaging.autonomous;

import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.ControlMessageType;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class AutonomousMessage extends ControlMessage {
    public AutonomousMessage(ControlMessageType messageType, String sender, String receiver) {
        super(messageType, sender, receiver);
    }
}
