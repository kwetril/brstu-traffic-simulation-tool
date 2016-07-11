package by.brstu.tst.core.by.brstu.tst.core.map;

/**
 * Created by kwetril on 7/7/16.
 */
public class Point {
    private int id;
    private float lon;
    private float lat;

    public Point(int id, float lon, float lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
    }

    public int getId() {
        return id;
    }

    public float getLon() {
        return lon;
    }

    public float getLat() {
        return lat;
    }
}
