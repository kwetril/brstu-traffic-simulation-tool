package by.brstu.tst.core.simulation.messaging.adaptive;

import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.ControlMessageType;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class AdaptiveControlMessage extends ControlMessage {
    public AdaptiveControlMessage(ControlMessageType messageType, String sender, String receiver) {
        super(messageType, sender, receiver);
    }
}
