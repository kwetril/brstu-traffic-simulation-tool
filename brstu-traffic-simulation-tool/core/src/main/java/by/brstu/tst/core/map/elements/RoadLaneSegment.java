package by.brstu.tst.core.map.elements;

import java.util.List;

/**
 * Created by kwetril on 7/7/16.
 */
public class RoadLaneSegment {
    private int id;
    private MapPoint startPoint;
    private MapPoint endPoint;

    private RoadLaneSegment leftLane;
    private RoadLaneSegment rightLane;

    private List<RoadLaneSegment> prevSegments;
    private List<RoadLaneSegment> nextSegments;
}
