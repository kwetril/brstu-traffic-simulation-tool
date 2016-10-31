package by.brstu.tst.core.simulation.messaging.autonomous;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.messaging.ControlMessage;

import static by.brstu.tst.core.simulation.messaging.ControlMessageType.AUTONOMOUS_REQUEST_PREFFERED_LANES;

/**
 * Created by a.klimovich on 31.10.2016.
 */
public class RequestPrefferedLanes extends ControlMessage {
    private DirectedRoad roadFrom;
    private DirectedRoad roadTo;

    public RequestPrefferedLanes(String vehicle, String intersection,
                                 DirectedRoad roadFrom, DirectedRoad roadTo) {
        super(AUTONOMOUS_REQUEST_PREFFERED_LANES, vehicle, intersection);
        this.roadFrom = roadFrom;
        this.roadTo = roadTo;
    }

    public DirectedRoad getRoadFrom() {
        return roadFrom;
    }

    public DirectedRoad getRoadTo() {
        return roadTo;
    }
}
