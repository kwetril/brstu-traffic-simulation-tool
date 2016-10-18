package by.brstu.tst.core.simulation.control.cyclic;

import by.brstu.tst.core.simulation.control.IntersectionState;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class CycleSection {
    private IntersectionState state;
    private float duration;

    public CycleSection(IntersectionState state, float duration) {
        this.state = state;
        this.duration = duration;
    }

    public IntersectionState getState() {
        return state;
    }

    public float getDuration() {
        return duration;
    }
}
