package by.brstu.tst.visualization;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kwetril on 7/5/16.
 */
public class SimulationMapPanel extends JPanel {

    private Map map;
    private float scale;
    private float positionLon;
    private float positionLat;

    private static float RoadLaneWidth = 0.5f;

    private static Color BACKGROUND_COLOR = new Color(0, 100, 0);

    public SimulationMapPanel(Map map) {
        this.map = map;
        setOpaque(true);
        setBackground(BACKGROUND_COLOR);
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D graphics2d = (Graphics2D) graphics;

        this.scale = Math.min(((float) getWidth()) / map.getWidth(),
                ((float) getHeight()) / map.getHeight());
        this.positionLon = map.getLongitude();
        this.positionLat = map.getLatitude();

        HashMap<Integer, MapPoint> points = map.getPoints();
        ArrayList<RoadSegment> segments = map.getSegments();
        float scaledWidth = RoadLaneWidth * scale;
        System.out.printf("Scaled width: %s\n", scaledWidth);
        System.out.printf("Scale: %s\n", scale);
        System.out.printf("Width: %s; Height: %s\n", getWidth(), getHeight());
        for (RoadSegment segment : segments) {
            MapPoint startPoint = points.get(segment.getStartPointId());
            MapPoint endPoint = points.get(segment.getEndPointId());
            graphics2d.setStroke(new BasicStroke(scaledWidth));
            graphics2d.draw(new Line2D.Float(lon2pos(startPoint.getLongitude()),
                    lat2pos(startPoint.getLatitude()),
                    lon2pos(endPoint.getLongitude()),
                    lat2pos(endPoint.getLatitude())));
        }

        for (MapPoint point : points.values()) {
            float circleWidth = (float) (scaledWidth * Math.sqrt(2));
            graphics2d.fillOval((int) (lon2pos(point.getLongitude()) - circleWidth / 2) ,
                    (int) (lat2pos(point.getLatitude()) - circleWidth / 2),
                     (int) circleWidth, (int) circleWidth);
        }
    }

    private float lon2pos(float longitude) {
        return (longitude - positionLon) * scale;
    }

    private float lat2pos(float latitude) {
        return (latitude - positionLat) * scale;
    }
}
