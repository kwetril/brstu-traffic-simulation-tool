package by.brstu.tst.core.simulation.control.adaptive;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.control.IntersectionController;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class AdaptiveIntersectionController implements IntersectionController {
    @Override
    public void updateInnerState(SimulationState simulationState, MessagingQueue messagingQueue) {

    }

    @Override
    public IntersectionState getStateByTime(float time) {
        return null;
    }

    @Override
    public Intersection getIntersection() {
        return null;
    }

    @Override
    public boolean connectorExist(DirectedRoad fromRoad, int fromLane, DirectedRoad toRoad) {
        return false;
    }
}
