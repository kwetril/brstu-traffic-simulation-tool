package by.brstu.tst.core.simulation.routing.state;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.simulation.routing.Route;
import by.brstu.tst.core.simulation.routing.info.RouteStateInfo;

/**
 * Created by a.klimovich on 08.10.2016.
 */
public class ChangingLaneState extends RouteInnerState {
    public ChangingLaneState(Route route, DirectedRoad road, int startLane, int segment, double curveParameter,
                             ChangeLaneType changeLaneType, double distance) {

    }

    @Override
    public double updateState(double distance) {
        return 0;
    }

    @Override
    public RouteStateInfo getStateInfo() {
        return null;
    }
}
