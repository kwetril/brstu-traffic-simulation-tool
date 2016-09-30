package by.brstu.tst.core.map.primitives;

/**
 * Created by kwetril on 7/7/16.
 *
 * Coordinates in EPSG:3785
 */
public class MapPoint {
    private double x;
    private double y;

    public MapPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceTo(MapPoint point) {
        double dx = x - point.x;
        double dy = y - point.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format("(%.2f; %.2f)", x, y);
    }
}
