package by.brstu.tst.core.simulation.messaging;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public enum ControlMessageType {
    CYCLIC_INTERSECTION_STATE,
    CYCLIC_GET_SUITABLE_LANES_REQUEST,
    CYCLIC_GET_SUITABLE_LANES_RESPONSE,

    AUTONOMOUS_REQUEST_DIRECTIONS,
    AUTONOMOUS_RESPONSE_DIRECTIONS,
    AUTONMOUS_INTERSECTION_COMMAND,
    AUTONOMOUS_INTERSECTION_PASSED,
    AUTONOMOUS_INTERSECTION_STATE,
    AUTONOMOUS_ENTER_INTERSECTION,
    AUTONOMOUS_REQUEST_PREFFERED_LANES,
    AUTONOMOUS_RESPONSE_PREFFERED_LANES
}
