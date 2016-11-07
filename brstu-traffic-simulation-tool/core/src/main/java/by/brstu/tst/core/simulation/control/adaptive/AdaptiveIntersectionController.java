package by.brstu.tst.core.simulation.control.adaptive;

import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.control.IControllerVisitor;
import by.brstu.tst.core.simulation.control.IntersectionController;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;
import by.brstu.tst.core.simulation.messaging.adaptive.AdaptiveNewVehicleNotification;
import by.brstu.tst.core.simulation.messaging.cyclic.BroadcastIntersectionStateMessage;
import by.brstu.tst.core.simulation.messaging.cyclic.GetSuitableLanesRequestMessage;
import by.brstu.tst.core.simulation.messaging.cyclic.GetSuitableLanesResponseMessage;
import com.google.common.collect.Iterables;

import java.util.*;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class AdaptiveIntersectionController implements IntersectionController {
    private Intersection intersection;
    private HashMap<String, IntersectionState> roadToState;
    private MovingCounters counters;
    private double period;
    private List<Map.Entry<String, Double>> roadToSectionEndTime;
    private double nextRecalculationTime;
    private double intermediateSectionTime;
    int currentStateIndex;
    private IntersectionState state;

    public AdaptiveIntersectionController(Intersection intersection,
                                          HashMap<String, IntersectionState> roadToState,
                                          double period, int historyMinutes) {
        this.intersection = intersection;
        counters = new MovingCounters(historyMinutes);
        this.period = period;
        this.roadToState = roadToState;
        roadToSectionEndTime = null;
        nextRecalculationTime = 0;
        intermediateSectionTime = 5.0;
    }

    @Override
    public void updateInnerState(SimulationState simulationState, MessagingQueue messagingQueue) {
        processMessages(messagingQueue, simulationState.getSimulationTime());
        updateSection(simulationState.getSimulationTime());
        updateIntersectionState(simulationState.getSimulationTime());
        messagingQueue.addMessage(new BroadcastIntersectionStateMessage(intersection.getName(), getState()));
    }

    private void processMessages(MessagingQueue messageQueue, double simulationTime) {
        Iterable<ControlMessage> messages = Iterables.filter(messageQueue.getCurrentMessages(),
                m -> !m.isBroadcast() && m.getReceiver().equals(intersection.getName()));
        for (ControlMessage message : messages) {
            switch (message.getType()) {
                case ADAPTIVE_NEW_VEHICLE_NOTIFICATION:
                    String road = ((AdaptiveNewVehicleNotification) message).getRoad();
                    counters.incrementCounter(road, simulationTime);
                    break;
                case CYCLIC_GET_SUITABLE_LANES_REQUEST:
                    GetSuitableLanesRequestMessage request = (GetSuitableLanesRequestMessage) message;
                    List<Integer> suitableLanes = new ArrayList<>();
                    for (int i = 0; i < request.getFromRoad().getNumLanes(); i++) {
                        if (
                        roadToState.get(request.getFromRoad().getName())
                                .getOpenedConnections().contains(new RoadConnectorDescription(request.getFromRoad(), i, request.getToRoad(), i))) {
                            suitableLanes.add(i);
                        }
                    }
                    messageQueue.addMessage(new GetSuitableLanesResponseMessage(request.getReceiver(),
                            request.getSender(), suitableLanes));
                    break;
                default:
                    throw new RuntimeException("Not supported message type");
            }
        }
    }

    private void updateSection(double simulationTime) {
        if (simulationTime >= nextRecalculationTime) {
            int totalCount = Math.max(counters.getTotalCount(), 1);
            roadToSectionEndTime = new ArrayList<>();
            int numNotEmptySections = 0;
            for (String road : roadToState.keySet()) {
                if (counters.getCountForRoad(road) > 0) {
                    numNotEmptySections++;
                }
            }
            boolean allSectionsEmpty = false;
            if (numNotEmptySections == 0) {
                numNotEmptySections = roadToState.size();
                allSectionsEmpty = true;
            }
            double activePeriod = period - numNotEmptySections * intermediateSectionTime;

            double offset = 0;
            for (String road : roadToState.keySet()) {
                int roadCount = counters.getCountForRoad(road);
                double sectionTime;
                if (roadCount == 0 && allSectionsEmpty) {
                    sectionTime = activePeriod / roadToState.size();
                }
                else {
                    sectionTime = activePeriod * roadCount / totalCount;
                }
                offset += sectionTime;
                roadToSectionEndTime.add(new AbstractMap.SimpleEntry<>(road, nextRecalculationTime + offset));
                offset += intermediateSectionTime;
            }
            nextRecalculationTime += period;
            currentStateIndex = 0;
            state = roadToState.get(roadToSectionEndTime.get(currentStateIndex).getKey());
        }
    }

    private void updateIntersectionState(double simulationTime) {
        while (currentStateIndex < roadToSectionEndTime.size()
                && simulationTime > roadToSectionEndTime.get(currentStateIndex).getValue() + intermediateSectionTime) {
            currentStateIndex++;
        }
        if (currentStateIndex >= roadToSectionEndTime.size()
                || simulationTime > roadToSectionEndTime.get(currentStateIndex).getValue()) {
            state = IntersectionState.EMPTY;
            return;
        }
        state = roadToState.get(roadToSectionEndTime.get(currentStateIndex).getKey());
    }

    @Override
    public IntersectionState getState() {
        if (state == null) {
            state = IntersectionState.EMPTY;
        }
        return state;
    }

    @Override
    public Intersection getIntersection() {
        return intersection;
    }

    @Override
    public void accept(IControllerVisitor visitor) {
        visitor.visit(this);
    }
}
