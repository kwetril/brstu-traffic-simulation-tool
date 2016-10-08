package by.brstu.tst.core.simulation.routing.state;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.elements.NodeRoadElement;
import by.brstu.tst.core.simulation.routing.Route;
import by.brstu.tst.core.simulation.routing.info.InnerNodeStateInfo;
import by.brstu.tst.core.simulation.routing.info.RouteStateInfo;

/**
 * Created by a.klimovich on 08.10.2016.
 */
public class InnerNodeState extends RouteInnerState {
    private Route route;
    private Intersection node;
    private DirectedRoad fromRoad;
    private int fromLane;
    private DirectedRoad toRoad;
    private int toLane;

    public InnerNodeState(Route route, NodeRoadElement node,
                          DirectedRoad fromRoad, int fromLane,
                          DirectedRoad toRoad, int toLane) {
        this.route = route;
        this.node = (Intersection) node;
        this.curve = this.node.getConnector(fromRoad, fromLane, toRoad, toLane);
        this.fromRoad = fromRoad;
        this.fromLane = fromLane;
        this.toRoad = toRoad;
        this.toLane = toLane;
    }

    @Override
    public double updateState(double distance) {
        return  updateStateOnCurve(distance);
    }

    @Override
    public RouteStateInfo getStateInfo() {
        return new InnerNodeStateInfo(route, node, toLane, curve, curveParameter);
    }
}
