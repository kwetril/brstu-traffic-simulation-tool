package by.brstu.tst.core.map.primitives;

/**
 * Created by kwetril on 7/7/16.
 */
public class MapPoint {
    private float lon;
    private float lat;

    public MapPoint(float lon, float lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public float getLat() {
        return lat;
    }

    public float calculateDistance(MapPoint point) {
        float dlon = lon - point.lon;
        float dlat = lat - point.lat;
        return (float) Math.sqrt(dlon * dlon + dlat * dlat);
    }
}
