package by.brstu.tst.core.map.primitives;

/**
 * Created by a.klimovich on 26.08.2016.
 */
public class MapRectangle {
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    public MapRectangle(double minLon, double minLat,
                        double maxLon, double maxLat) {
        this.minX = minLon;
        this.minY = minLat;
        this.maxX = maxLon;
        this.maxY = maxLat;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getWidth() {
        return maxX - minX;
    }

    public double getHeight() {
        return maxY - minY;
    }
}
