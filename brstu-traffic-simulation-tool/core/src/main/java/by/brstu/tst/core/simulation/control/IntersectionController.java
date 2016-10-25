package by.brstu.tst.core.simulation.control;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.simulation.messaging.IMessagingAgent;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public interface IntersectionController extends IMessagingAgent {
    IntersectionState getState();
    Intersection getIntersection();
    void accept(IControllerVisitor visitor);
}
