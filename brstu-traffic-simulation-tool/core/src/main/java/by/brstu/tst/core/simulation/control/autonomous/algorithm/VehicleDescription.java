package by.brstu.tst.core.simulation.control.autonomous.algorithm;

import by.brstu.tst.core.map.utils.RoadConnectorDescription;

/**
 * Created by a.klimovich on 23.10.2016.
 */
class VehicleDescription {
    private String name;
    private double distance;
    private RoadConnectorDescription connectorDescription;
    private double waitingTime;

    VehicleDescription(String name, double distance, RoadConnectorDescription connectorDescription,
                       double waitingTime) {
        this.name = name;
        this.distance = distance;
        this.connectorDescription = connectorDescription;
        this.waitingTime = waitingTime;
    }

    String getId() {
        return name;
    }

    double getDistance() {
        return distance;
    }

    RoadConnectorDescription getConnectorDescription() {
        return connectorDescription;
    }

    double getWaitingTime() {
        return waitingTime;
    }
}
