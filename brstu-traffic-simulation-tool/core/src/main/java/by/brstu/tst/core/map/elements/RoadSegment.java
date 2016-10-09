package by.brstu.tst.core.map.elements;

import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.utils.MapUtils;

/**
 * Created by a.klimovich on 30.09.2016.
 */
public class RoadSegment {
    private DirectedRoad road;
    private int index;
    private BezierCurve centerCurve;
    private int numLanes;
    private RoadLaneSegment[] lanes;

    public RoadSegment(DirectedRoad road, int index, BezierCurve centerCurve, int numLanes) {
        this.road = road;
        this.index = index;
        this.centerCurve = centerCurve;
        this.numLanes = numLanes;
        lanes = new RoadLaneSegment[numLanes];
        for (int i = 0; i < numLanes; i++) {
            lanes[i] = new RoadLaneSegment(this, i, calculateLaneCurve(i));
        }
    }

    private BezierCurve calculateLaneCurve(int laneNumber) {
        float numLanesFromCenter = (laneNumber - numLanes) + numLanes / 2.0f + 0.5f;
        float distanceFromCenter = numLanesFromCenter * road.getLaneWidth();
        if (distanceFromCenter >= 0) {
            return MapUtils.moveCurveRight(centerCurve, distanceFromCenter);
        } else {
            return MapUtils.moveCurveLeft(centerCurve, -distanceFromCenter);
        }
    }

    public RoadLaneSegment getLane(int laneNumber) {
        return lanes[laneNumber];
    }

    public DirectedRoad getRoad() {
        return road;
    }

    public int getIndex() {
        return index;
    }

    public BezierCurve getCenterCurve() {
        return centerCurve;
    }

    public int getNumLanes() {
        return numLanes;
    }
}
