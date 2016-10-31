package by.brstu.tst.core.simulation.messaging.autonomous;

import by.brstu.tst.core.simulation.messaging.ControlMessage;

import java.util.HashMap;

import static by.brstu.tst.core.simulation.messaging.ControlMessageType.AUTONOMOUS_RESPONSE_PREFFERED_LANES;

/**
 * Created by a.klimovich on 31.10.2016.
 */
public class ResponsePrefferedLanes extends ControlMessage {
    private HashMap<Integer, Double> laneToPriority;

    public ResponsePrefferedLanes(String intersection, String vehicle, HashMap<Integer, Double> laneToPriority) {
        super(AUTONOMOUS_RESPONSE_PREFFERED_LANES, intersection, vehicle);
        this.laneToPriority = laneToPriority;
    }

    public HashMap<Integer, Double> getLaneToPriority() {
        return laneToPriority;
    }
}
