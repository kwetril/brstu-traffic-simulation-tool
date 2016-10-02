package by.brstu.tst.core.map.utils;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class RoadConnectorDescription {
    private String from;
    private int fromLane;
    private String to;
    private int toLane;

    private String strValue;

    public RoadConnectorDescription(String from, int fromLane, String to, int toLane) {
        this.from = from;
        this.fromLane = fromLane;
        this.to = to;
        this.toLane = toLane;

        strValue = String.format("%s[%s] - %s[%s]", from, fromLane, to, toLane);
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
