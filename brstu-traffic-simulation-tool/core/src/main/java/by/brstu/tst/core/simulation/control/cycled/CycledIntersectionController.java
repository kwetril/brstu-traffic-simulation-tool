package by.brstu.tst.core.simulation.control.cycled;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.control.IntersectionController;
import by.brstu.tst.core.simulation.control.IntersectionState;

import java.util.HashSet;
import java.util.List;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class CycledIntersectionController implements IntersectionController {
    private Intersection intersection;
    private List<CycleSection> cycleSections;
    private float period;
    private HashSet<RoadConnectorDescription> existingConnectors;

    public CycledIntersectionController(Intersection intersection, List<CycleSection> cycleSections) {
        this.intersection = intersection;
        this.cycleSections = cycleSections;
        existingConnectors = new HashSet<>();
        period = 0;
        for (CycleSection section : cycleSections) {
            period += section.getDuration();
            existingConnectors.addAll(section.getState().getOpenedConnections());
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

    @Override
    public Intersection getIntersection() {
        return intersection;
    }

    @Override
    public boolean connectorExist(DirectedRoad fromRoad, int fromLane, DirectedRoad toRoad) {
        int toLane = Math.min(toRoad.getNumLanes(), fromLane);
        return existingConnectors.contains(new RoadConnectorDescription(fromRoad.getName(), fromLane,
                toRoad.getName(), toLane));
    }
}
