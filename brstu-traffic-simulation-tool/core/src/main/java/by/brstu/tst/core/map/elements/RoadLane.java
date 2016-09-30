package by.brstu.tst.core.map.elements;

import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.map.utils.MapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwetril on 9/8/16.
 */
public class RoadLane {
    private DirectedRoad parentRoad;
    private int number;
    private MapPoint startPoint;
    private MapPoint endPoint;
    private List<RoadLaneSegment> segments;

    public RoadLane(DirectedRoad parentRoad, int number) {
        this.parentRoad = parentRoad;
        this.number = number;
        calculateCurve();
    }

    private void calculateCurve() {
        float numLanesFromCenter = (number - parentRoad.getNumLanes()) + parentRoad.getNumLanes() / 2.0f + 0.5f;
        float distanceFromCenter = numLanesFromCenter * parentRoad.getLaneWidth();
        Vector roadVector = new Vector(parentRoad.getStartPoint(), parentRoad.getEndPoint());
        startPoint = MapUtils.GetLeftPoint(parentRoad.getStartPoint(), roadVector, distanceFromCenter);
        endPoint = MapUtils.GetLeftPoint(parentRoad.getEndPoint(), roadVector, distanceFromCenter);

    }

    public MapPoint getStartPoint() {
        return startPoint;
    }

    public MapPoint getEndPoint() {
        return endPoint;
    }
}
