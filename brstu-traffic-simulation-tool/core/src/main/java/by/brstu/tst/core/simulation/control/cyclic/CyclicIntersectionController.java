package by.brstu.tst.core.simulation.control.cyclic;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.control.IntersectionController;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.cyclic.BroadcastIntersectionStateMessage;
import by.brstu.tst.core.simulation.messaging.cyclic.GetSuitableLanesRequestMessage;
import by.brstu.tst.core.simulation.messaging.cyclic.GetSuitableLanesResponseMessage;
import com.google.common.collect.Iterables;

import java.util.*;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class CyclicIntersectionController implements IntersectionController {
    private Intersection intersection;
    private List<CycleSection> cycleSections;
    private float period;
    private HashSet<RoadConnectorDescription> existingConnectors;

    public CyclicIntersectionController(Intersection intersection, List<CycleSection> cycleSections) {
        this.intersection = intersection;
        this.cycleSections = cycleSections;
        existingConnectors = new HashSet<>();
        period = 0;
        for (CycleSection section : cycleSections) {
            period += section.getDuration();
            existingConnectors.addAll(section.getState().getOpenedConnections());
        }
    }

    @Override
    public IntersectionState getStateByTime(float time) {
        while (time > period) {
            time -= period;
        }
        for (CycleSection section : cycleSections) {
            if (section.getDuration() > time) {
                return section.getState();
            } else {
                time -= section.getDuration();
            }
        }
        return cycleSections.get(0).getState();
    }

    @Override
    public Intersection getIntersection() {
        return intersection;
    }

    @Override
    public boolean connectorExist(DirectedRoad fromRoad, int fromLane, DirectedRoad toRoad) {
        int toLane = Math.min(toRoad.getNumLanes(), fromLane);
        return existingConnectors.contains(new RoadConnectorDescription(fromRoad.getName(), fromLane,
                toRoad.getName(), toLane));
    }

    @Override
    public Iterable<ControlMessage> updateInnerState(SimulationState simulationState, Iterable<ControlMessage> inputMessages) {
        Iterable<ControlMessage> messagesToProcess = Iterables.filter(inputMessages,
                message -> !message.isBroadcast() && message.getReceiver().equals(intersection.getName()));
        List<ControlMessage> outpuMessages = new ArrayList<>();
        outpuMessages.add(new BroadcastIntersectionStateMessage(intersection.getName(),
                getStateByTime(simulationState.getSimulationTime())));
        for (ControlMessage message : messagesToProcess) {
            switch (message.getType()) {
                case CYCLIC_GET_SUITABLE_LANES_REQUEST:
                    GetSuitableLanesRequestMessage request = (GetSuitableLanesRequestMessage) message;
                    List<Integer> suitableLanes = new ArrayList<>();
                    for (int i = 0; i < request.getFromRoad().getNumLanes(); i++) {
                        if (connectorExist(request.getFromRoad(), i, request.getToRoad())) {
                            suitableLanes.add(i);
                        }
                    }
                    outpuMessages.add(new GetSuitableLanesResponseMessage(request.getReceiver(),
                            request.getSender(), suitableLanes));
                    break;
                default:
                    throw new RuntimeException("Not supported message type");
            }
        }
        return outpuMessages;
    }
}
