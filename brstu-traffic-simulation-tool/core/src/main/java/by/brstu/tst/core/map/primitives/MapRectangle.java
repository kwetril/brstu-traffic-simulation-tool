package by.brstu.tst.core.map.primitives;

/**
 * Created by a.klimovich on 26.08.2016.
 */
public class MapRectangle {
    private float minX;
    private float maxX;
    private float minY;
    private float maxY;

    public MapRectangle(float minLon, float minLat,
                        float maxLon, float maxLat) {
        this.minX = minLon;
        this.minY = minLat;
        this.maxX = maxLon;
        this.maxY = maxLat;
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getWidth() {
        return maxX - minX;
    }

    public float getHeight() {
        return maxY - minY;
    }
}
