package by.brstu.tst.core.map.primitives;

/**
 * Created by kwetril on 7/7/16.
 *
 * Coordinates in EPSG:3785
 */
public class MapPoint {
    private float x;
    private float y;

    public MapPoint(float lon, float lat) {
        this.x = lon;
        this.y = lat;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float calculateDistance(MapPoint point) {
        float dlon = x - point.x;
        float dlat = y - point.y;
        return (float) Math.sqrt(dlon * dlon + dlat * dlat);
    }
}
