package by.brstu.tst.core.simulation.control.autonomous.algorithm;

import by.brstu.tst.core.map.utils.RoadConnectorDescription;

/**
 * Created by a.klimovich on 23.10.2016.
 */
public class VehicleDescription {
    private String name;
    private double distance;
    private RoadConnectorDescription connectorDescription;
    private double waitingTime;

    public VehicleDescription(String name, double distance, RoadConnectorDescription connectorDescription,
                              double waitingTime) {
        this.name = name;
        this.distance = distance;
        this.connectorDescription = connectorDescription;
        this.waitingTime = waitingTime;
    }

    public String getId() {
        return name;
    }

    public double getDistance() {
        return distance;
    }

    public RoadConnectorDescription getConnectorDescription() {
        return connectorDescription;
    }

    public double getWaitingTime() {
        return waitingTime;
    }
}
