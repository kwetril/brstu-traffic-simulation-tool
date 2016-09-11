package by.brstu.tst.core.map.elements;

import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.map.utils.MapUtils;

/**
 * Created by kwetril on 9/8/16.
 */
public class RoadLane {
    private DirectedRoad parentRoad;
    private int number;
    private MapPoint startPoint;
    private MapPoint endPoint;

    public RoadLane(DirectedRoad parentRoad, int number) {
        this.parentRoad = parentRoad;
        this.number = number;
        calculatePointsPosition();
    }

    private void calculatePointsPosition() {
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
