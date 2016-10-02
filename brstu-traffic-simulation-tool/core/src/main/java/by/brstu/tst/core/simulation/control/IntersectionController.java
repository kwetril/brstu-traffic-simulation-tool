package by.brstu.tst.core.simulation.control;

import by.brstu.tst.core.map.elements.Intersection;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public interface IntersectionController {
    IntersectionState getStateByTime(float time);
    Intersection getIntersection();
}
