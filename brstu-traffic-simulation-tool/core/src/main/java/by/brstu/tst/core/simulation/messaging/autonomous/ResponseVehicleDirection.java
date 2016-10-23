package by.brstu.tst.core.simulation.messaging.autonomous;

import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.messaging.ControlMessage;

import static by.brstu.tst.core.simulation.messaging.ControlMessageType.AUTONOMOUS_RESPONSE_DIRECTIONS;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class ResponseVehicleDirection extends ControlMessage {
    private MapPoint position;
    private RoadConnectorDescription connectorDescription;

    public ResponseVehicleDirection(String sender, String receiver,
                                    MapPoint position, RoadConnectorDescription connectorDescription) {
        super(AUTONOMOUS_RESPONSE_DIRECTIONS, sender, receiver);
        this.position = position;
        this.connectorDescription = connectorDescription;
    }

    public MapPoint getPosition() {
        return position;
    }

    public RoadConnectorDescription getConnectorDescription() {
        return connectorDescription;
    }
}
