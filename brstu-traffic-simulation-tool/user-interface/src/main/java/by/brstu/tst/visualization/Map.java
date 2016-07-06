package by.brstu.tst.visualization;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by kwetril on 7/6/16.
 */
public class Map {
    private float longitude;
    private float latitude;
    private float width;
    private float height;

    private HashMap<Integer, MapPoint> points;
    private ArrayList<RoadSegment> segments;

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Map(float longitude, float latitude, float height, float width) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.height = height;
        this.width = width;
        points = new HashMap<>();
        segments = new ArrayList<>();
    }

    public HashMap<Integer, MapPoint> getPoints() {
        return points;
    }

    public ArrayList<RoadSegment> getSegments() {
        return segments;
    }

    public static Map GetTestMap() {
        Map map = new Map(10520f, 8519f, 8f, 10f);
        HashMap<Integer, MapPoint> points = map.getPoints();
        MapPoint point1 = new MapPoint(1, 10521.53f, 8522.60f);
        points.put(point1.getId(), point1);
        MapPoint point2 = new MapPoint(2, 10523.21f, 8524.24f);
        points.put(point2.getId(), point2);
        MapPoint point3 = new MapPoint(3, 10526.75f, 8522.29f);
        points.put(point3.getId(), point3);
        MapPoint point4 = new MapPoint(4, 10523.70f, 8520.15f);
        points.put(point4.getId(), point4);
        MapPoint point5 = new MapPoint(5, 10524.08f, 8527.84f);
        points.put(point5.getId(), point5);

        ArrayList<RoadSegment> segments = map.getSegments();
        RoadSegment segment1 = new RoadSegment(point1.getId(), point2.getId());
        segments.add(segment1);
        RoadSegment segment2 = new RoadSegment(point2.getId(), point3.getId());
        segments.add(segment2);
        RoadSegment segment3 = new RoadSegment(point4.getId(), point2.getId());
        segments.add(segment3);
        RoadSegment segment4 = new RoadSegment(point2.getId(), point5.getId());
        segments.add(segment4);

        return map;
    }
}
