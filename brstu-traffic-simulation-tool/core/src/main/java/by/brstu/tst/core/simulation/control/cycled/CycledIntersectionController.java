package by.brstu.tst.core.simulation.control.cycled;

import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.simulation.control.IntersectionController;
import by.brstu.tst.core.simulation.control.IntersectionState;

import java.util.List;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class CycledIntersectionController implements IntersectionController {
    private List<CycleSection> cycleSections;
    private float period;

    public CycledIntersectionController(Intersection intersection, List<CycleSection> cycleSections) {
        this.cycleSections = cycleSections;
        period = 0;
        for (CycleSection section : cycleSections) {
            period += section.getDuration();
        }
    }

    @Override
    public IntersectionState getStateByTime(float time) {
        while (time > period) {
            time -= period;
        }
        for (CycleSection section : cycleSections) {
            if (section.getDuration() > time) {
                return section.getState();
            } else {
                time -= section.getDuration();
            }
        }
        return cycleSections.get(0).getState();
    }
}
