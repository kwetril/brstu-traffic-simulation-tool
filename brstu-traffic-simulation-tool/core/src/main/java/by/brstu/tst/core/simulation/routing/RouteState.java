package by.brstu.tst.core.simulation.routing;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.NodeRoadElement;
import by.brstu.tst.core.simulation.routing.info.RouteStateInfo;
import by.brstu.tst.core.simulation.routing.state.*;

/**
 * Created by a.klimovich on 08.10.2016.
 */
public class RouteState {
    private RouteInnerState innerState;
    private final double eps = 0.001;
    private int nextRoadLane;

    public RouteState(Route route, int initialLane) {
        NodeRoadElement source = route.getSource();
        DirectedRoad road = route.getNextRoad(source);
        nextRoadLane = initialLane;
        innerState = new DirectedRoadState(route, road, initialLane, 0, 0.0);
    }

    public void changeLane(ChangeLaneType changeLaneType, double changingLaneDistance) {
        RouteStateInfo stateInfo = innerState.getStateInfo();
        if (!stateInfo.isOnRoad() || stateInfo.isChangingLane()) {
            return;
        }
        int nextLane = stateInfo.getLane();
        if (changeLaneType == ChangeLaneType.LEFT) {
            nextLane = Math.max(0, nextLane - 1);
        } else {
            nextLane = Math.min(stateInfo.getNumLanes() - 1, nextLane + 1);
        }
        if (nextLane == stateInfo.getLane()) {
            return;
        }
        nextRoadLane = nextLane;
        innerState = new ChangingLaneState(stateInfo.getRoute(), stateInfo.getCurrentRoad(),
                stateInfo.getLane(), stateInfo.getCurrentSegment(), stateInfo.getCurveParameter(),
                changeLaneType, changingLaneDistance);
    }

    public void setNextRoadLane(int lane) {
        nextRoadLane = lane;
    }

    public void updatePosition(double distance) {
        while (distance > eps) {
            distance = innerState.updateState(distance);
            if (distance > eps) {
                changeInnerState();
            }
        }
    }

    private void changeInnerState() {
        RouteStateInfo stateInfo = innerState.getStateInfo();
        if (stateInfo.isChangingLane()) {
            innerState = new DirectedRoadState(stateInfo.getRoute(), stateInfo.getCurrentRoad(),
                    stateInfo.getLaneAfterChange(), stateInfo.getSegmentAfterLaneChange(),
                    stateInfo.getCurveParameterAfterLaneChange());
        } else if (stateInfo.isOnRoad()) {
            if (stateInfo.getNextRoad() == null) {
                innerState = new ReachedDestinationState(stateInfo.getRoute());
            } else {
                nextRoadLane = Math.min(nextRoadLane, stateInfo.getNextRoad().getNumLanes());
                innerState = new InnerNodeState(stateInfo.getRoute(), stateInfo.getNextNode(),
                        stateInfo.getCurrentRoad(), stateInfo.getLane(),
                        stateInfo.getNextRoad(), nextRoadLane);
            }
        } else {
            innerState = new DirectedRoadState(stateInfo.getRoute(), stateInfo.getNextRoad(), stateInfo.getLaneAfterNode());
        }
    }

    public RouteStateInfo getStateInfo() {
        return innerState.getStateInfo();
    }
}
