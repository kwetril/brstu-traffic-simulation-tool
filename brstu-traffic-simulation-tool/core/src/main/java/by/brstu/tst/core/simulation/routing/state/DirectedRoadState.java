package by.brstu.tst.core.simulation.routing.state;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.simulation.routing.Route;
import by.brstu.tst.core.simulation.routing.info.DirectedRoadStateInfo;
import by.brstu.tst.core.simulation.routing.info.RouteStateInfo;

/**
 * Created by a.klimovich on 08.10.2016.
 */
public class DirectedRoadState extends RouteInnerState {
    private Route route;
    private DirectedRoad road;
    private int lane;
    private int segment;

    public DirectedRoadState(Route route, DirectedRoad road, int lane) {
        this(route, road, lane, 0, 0.0);
    }

    public DirectedRoadState(Route route, DirectedRoad road, int lane, int segment, double curveParameter) {
        this.route = route;
        this.road = road;
        this.lane = lane;
        this.segment = segment;
        this.curve = road.getSegments()[segment].getLane(lane).getCurve();
        this.curveParameter = curveParameter;
    }

    @Override
    public double updateState(double distance) {
        while (distance > EPS) {
            distance = updateStateOnCurve(distance);
            if (distance > EPS) {
                if (segment + 1 < road.getSegments().length) {
                    segment++;
                    curve = road.getSegments()[segment].getLane(lane).getCurve();
                    curveParameter = 0.0;
                } else {
                    return distance;
                }
            }
        }
        return distance;
    }

    @Override
    public RouteStateInfo getStateInfo() {
        return new DirectedRoadStateInfo(route, road, lane, segment, curveParameter);
    }
}
