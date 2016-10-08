package by.brstu.tst.core.simulation.routing.state;

import by.brstu.tst.core.simulation.routing.Route;
import by.brstu.tst.core.simulation.routing.info.DestinationReachedStateInfo;
import by.brstu.tst.core.simulation.routing.info.RouteStateInfo;

/**
 * Created by a.klimovich on 08.10.2016.
 */
public class ReachedDestinationState extends RouteInnerState {
    private Route route;

    public ReachedDestinationState(Route route) {
        this.route = route;
    }

    @Override
    public double updateState(double distance) {
        return 0;
    }

    @Override
    public RouteStateInfo getStateInfo() {
        return new DestinationReachedStateInfo(route);
    }
}
