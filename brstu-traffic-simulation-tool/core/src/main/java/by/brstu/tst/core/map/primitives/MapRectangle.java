package by.brstu.tst.core.map.primitives;

/**
 * Created by a.klimovich on 26.08.2016.
 */
public class MapRectangle {
    private float minLon;
    private float maxLon;
    private float minLat;
    private float maxLat;

    public MapRectangle(float minLon, float minLat,
                        float maxLon, float maxLat) {
        this.minLon = minLon;
        this.minLat = minLat;
        this.maxLon = maxLon;
        this.maxLat = maxLat;
    }

    public float getMinLon() {
        return minLon;
    }

    public float getMaxLon() {
        return maxLon;
    }

    public float getMinLat() {
        return minLat;
    }

    public float getMaxLat() {
        return maxLat;
    }
}
