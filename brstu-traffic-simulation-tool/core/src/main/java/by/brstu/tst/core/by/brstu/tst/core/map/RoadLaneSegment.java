package by.brstu.tst.core.by.brstu.tst.core.map;

import java.util.List;

/**
 * Created by kwetril on 7/7/16.
 */
public class RoadLaneSegment {
    private int id;
    private Point startPoint;
    private Point endPoint;

    private RoadLaneSegment leftLane;
    private RoadLaneSegment rightLane;

    private List<RoadLaneSegment> prevSegments;
    private List<RoadLaneSegment> nextSegments;
}
