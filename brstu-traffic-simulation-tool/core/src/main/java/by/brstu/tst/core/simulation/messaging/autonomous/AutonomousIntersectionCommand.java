package by.brstu.tst.core.simulation.messaging.autonomous;

import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.ControlMessageType;

import static by.brstu.tst.core.simulation.messaging.ControlMessageType.AUTONMOUS_INTERSECTION_COMMAND;

/**
 * Created by a.klimovich on 24.10.2016.
 */
public class AutonomousIntersectionCommand extends ControlMessage {
    private int sectionId;
    public AutonomousIntersectionCommand(String sender, String receiver, int sectionId) {
        super(AUTONMOUS_INTERSECTION_COMMAND, sender, receiver);
        this.sectionId = sectionId;
    }

    public int getSectionId() {
        return sectionId;
    }
}
