package by.brstu.tst.core.map.utils;

import by.brstu.tst.core.map.elements.DirectedRoad;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class RoadConnectorDescription {
    private DirectedRoad from;
    private int fromLane;

    public DirectedRoad getFrom() {
        return from;
    }

    public int getFromLane() {
        return fromLane;
    }

    public int getToLane() {
        return toLane;
    }

    public DirectedRoad getTo() {
        return to;
    }

    private DirectedRoad to;
    private int toLane;

    private String strValue;

    public RoadConnectorDescription(DirectedRoad from, int fromLane, DirectedRoad to, int toLane) {
        this.from = from;
        this.fromLane = fromLane;
        this.to = to;
        this.toLane = toLane;

        strValue = String.format("%s[%s] - %s[%s]", from.getName(), fromLane, to.getName(), toLane);
    }

    @Override
    public boolean equals(Object obj) {
        RoadConnectorDescription connector = (RoadConnectorDescription) obj;
        return strValue.equals(connector.strValue);
    }

    @Override
    public int hashCode() {
        return strValue.hashCode();
    }
}
