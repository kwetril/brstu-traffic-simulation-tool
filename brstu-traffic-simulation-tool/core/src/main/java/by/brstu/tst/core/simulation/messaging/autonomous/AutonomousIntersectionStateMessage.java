package by.brstu.tst.core.simulation.messaging.autonomous;

import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.ControlMessageType;

import static by.brstu.tst.core.simulation.messaging.ControlMessageType.AUTONOMOUS_INTERSECTION_STATE;

/**
 * Created by a.klimovich on 26.10.2016.
 */
public class AutonomousIntersectionStateMessage extends ControlMessage {
    private IntersectionState state;
    private int currentSectionId;
    private double averageSectionTime;
    public AutonomousIntersectionStateMessage(String sender, IntersectionState state,
                                              int currentSectionId, double averageSectionTime) {
        super(AUTONOMOUS_INTERSECTION_STATE, sender, null);
        this.state = state;
        this.currentSectionId = currentSectionId;
        this.averageSectionTime = averageSectionTime;
    }
    public IntersectionState getState() {
        return state;
    }

    public int getCurrentSectionId() {
        return currentSectionId;
    }

    public double getAverageSectionTime() {
        return averageSectionTime;
    }
}
