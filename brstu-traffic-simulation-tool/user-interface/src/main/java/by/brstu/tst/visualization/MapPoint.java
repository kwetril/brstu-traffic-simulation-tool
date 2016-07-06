package by.brstu.tst.visualization;

/**
 * Created by kwetril on 7/6/16.
 */
public class MapPoint {
    private int id;
    private float longitude;
    private float latitude;

    public MapPoint(int id, float longitude, float latitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }
}
