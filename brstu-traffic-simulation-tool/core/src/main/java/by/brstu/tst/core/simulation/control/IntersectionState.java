package by.brstu.tst.core.simulation.control;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.routing.state.DirectedRoadState;

import java.util.HashSet;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class IntersectionState {
    public static final IntersectionState EMPTY = new IntersectionState(new HashSet<>());

    protected HashSet<RoadConnectorDescription> openedConnections;

    public IntersectionState(HashSet<RoadConnectorDescription> state) {
        this.openedConnections = state;
    }

    public boolean isOpened(RoadConnectorDescription connectorDescription) {
        return openedConnections.contains(connectorDescription);
    }

    public boolean isOpened(DirectedRoad from, int laneFrom, DirectedRoad to, int laneTo) {
        return isOpened(new RoadConnectorDescription(from, laneFrom, to, laneTo));
    }

    public HashSet<RoadConnectorDescription> getOpenedConnections() {
        return openedConnections;
    }
}
