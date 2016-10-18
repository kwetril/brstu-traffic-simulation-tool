package by.brstu.tst.core.simulation.messaging.cyclic;

import by.brstu.tst.core.simulation.messaging.ControlMessage;

import java.util.List;

import static by.brstu.tst.core.simulation.messaging.ControlMessageType.CYCLIC_GET_SUITABLE_LANES_RESPONSE;

/**
 * Created by a.klimovich on 18.10.2016.
 */
public class GetSuitableLanesResponseMessage extends ControlMessage {
    private List<Integer> suitableLanes;

    public GetSuitableLanesResponseMessage(String intersectionName, String vehicleId,
                                           List<Integer> suitableLanes) {
        super(CYCLIC_GET_SUITABLE_LANES_RESPONSE, intersectionName, vehicleId);
        this.suitableLanes = suitableLanes;
    }

    public List<Integer> getSuitableLanes() {
        return suitableLanes;
    }
}
