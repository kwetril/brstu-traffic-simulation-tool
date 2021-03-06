package by.brstu.tst.core.simulation.messaging;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public enum ControlMessageType {
    BROADCAST_INTERSECTION_STATE,

    CYCLIC_GET_SUITABLE_LANES_REQUEST,
    CYCLIC_GET_SUITABLE_LANES_RESPONSE,

    ADAPTIVE_NEW_VEHICLE_NOTIFICATION,

    AUTONOMOUS_REQUEST_DIRECTIONS,
    AUTONOMOUS_RESPONSE_DIRECTIONS,
    AUTONOMOUS_INTERSECTION_PASSED,
    AUTONOMOUS_REQUEST_PREFFERED_LANES,
    AUTONOMOUS_RESPONSE_PREFFERED_LANES
}
