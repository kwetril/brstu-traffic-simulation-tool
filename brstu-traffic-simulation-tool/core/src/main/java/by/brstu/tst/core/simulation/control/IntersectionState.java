package by.brstu.tst.core.simulation.control;

import by.brstu.tst.core.map.utils.RoadConnectorDescription;

import java.util.HashSet;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class IntersectionState {
    private HashSet<RoadConnectorDescription> openedConnections;

    public IntersectionState(HashSet<RoadConnectorDescription> state) {
        this.openedConnections = state;
    }

    public boolean isOpened(RoadConnectorDescription connectorDescription) {
        return openedConnections.contains(connectorDescription);
    }
}
