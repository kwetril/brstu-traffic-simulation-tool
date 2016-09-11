package by.brstu.tst.core.map.primitives;

/**
 * Created by kwetril on 7/7/16.
 *
 * Coordinates in EPSG:3785
 */
public class MapPoint {
    private double x;
    private double y;

    public MapPoint(double lon, double lat) {
        this.x = lon;
        this.y = lat;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double calculateDistance(MapPoint point) {
        double dlon = x - point.x;
        double dlat = y - point.y;
        return Math.sqrt(dlon * dlon + dlat * dlat);
    }
}
