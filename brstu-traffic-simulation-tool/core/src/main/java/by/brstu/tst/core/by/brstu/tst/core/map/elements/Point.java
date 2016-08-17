package by.brstu.tst.core.by.brstu.tst.core.map.elements;

/**
 * Created by kwetril on 7/7/16.
 */
public class Point {
    private float lon;
    private float lat;

    public Point(float lon, float lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public float getLat() {
        return lat;
    }
}
