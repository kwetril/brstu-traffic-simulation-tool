package by.brstu.tst.core.simulation.messaging.autonomous;

import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.ControlMessageType;

import static by.brstu.tst.core.simulation.messaging.ControlMessageType.AUTONOMOUS_INTERSECTION_PASSED;

/**
 * Created by a.klimovich on 24.10.2016.
 */
public class IntersectionPassedNotification extends ControlMessage {
    public IntersectionPassedNotification(String sender, String receiver) {
        super(AUTONOMOUS_INTERSECTION_PASSED, sender, receiver);
    }
}
