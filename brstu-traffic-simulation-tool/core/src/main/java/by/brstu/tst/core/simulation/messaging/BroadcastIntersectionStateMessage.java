package by.brstu.tst.core.simulation.messaging;

import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.ControlMessageType;

/**
 * Created by a.klimovich on 18.10.2016.
 */
public class BroadcastIntersectionStateMessage extends ControlMessage {
    private IntersectionState intersectionState;

    public BroadcastIntersectionStateMessage(String intersectionName,
                                             IntersectionState intersectionState) {
        super(ControlMessageType.BROADCAST_INTERSECTION_STATE, intersectionName, null);
        this.intersectionState = intersectionState;
    }

    public IntersectionState getIntersectionState() {
        return intersectionState;
    }
}
