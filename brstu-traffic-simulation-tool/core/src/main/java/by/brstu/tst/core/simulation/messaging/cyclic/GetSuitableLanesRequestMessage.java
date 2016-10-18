package by.brstu.tst.core.simulation.messaging.cyclic;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.simulation.messaging.ControlMessage;

import static by.brstu.tst.core.simulation.messaging.ControlMessageType.CYCLIC_GET_SUITABLE_LANES_REQUEST;

/**
 * Created by a.klimovich on 18.10.2016.
 */
public class GetSuitableLanesRequestMessage extends ControlMessage {
    private DirectedRoad fromRoad;
    private DirectedRoad toRoad;

    public GetSuitableLanesRequestMessage(String vehicleId, String intersectionName,
                                          DirectedRoad fromRoad, DirectedRoad toRoad) {
        super(CYCLIC_GET_SUITABLE_LANES_REQUEST, vehicleId, intersectionName);
        this.fromRoad = fromRoad;
        this.toRoad = toRoad;
    }

    public DirectedRoad getFromRoad() {
        return fromRoad;
    }

    public DirectedRoad getToRoad() {
        return toRoad;
    }
}
