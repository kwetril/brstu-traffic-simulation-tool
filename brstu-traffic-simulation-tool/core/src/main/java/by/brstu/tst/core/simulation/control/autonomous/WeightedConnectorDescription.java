package by.brstu.tst.core.simulation.control.autonomous;

import by.brstu.tst.core.map.utils.RoadConnectorDescription;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class WeightedConnectorDescription {
    private RoadConnectorDescription connectorDescription;
    private double weight;
    public WeightedConnectorDescription(RoadConnectorDescription connectorDescription, double weight) {
        this.connectorDescription = connectorDescription;
        this.weight = weight;
    }

    public RoadConnectorDescription getConnectorDescription() {
        return connectorDescription;
    }

    public double getWeight() {
        return weight;
    }
}
