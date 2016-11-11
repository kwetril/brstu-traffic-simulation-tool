package by.brstu.tst.core.simulation.control.autonomous;

import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.control.IControllerVisitor;
import by.brstu.tst.core.simulation.control.IntersectionController;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.control.autonomous.algorithm.StateRecalculationAlgorithm;
import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;
import by.brstu.tst.core.simulation.messaging.autonomous.*;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class AutonomousIntersectionController implements IntersectionController {
    private Intersection intersection;
    private double nextRecalculationTime;
    private double recalculationCollectionTime;
    private double recalculationPeriod;
    private double operationRange;
    private StateRecalculationAlgorithm stateRecalculationAlgorithm;
    private HashMap<RoadConnectorDescription, HashSet<RoadConnectorDescription>> nonConflictConnectors;
    private double simulationTime;
    private MessagingQueue messagingQueue;

    public AutonomousIntersectionController(Intersection intersection, double recalculationPeriod,
                                            double operationRange) {
        this.intersection = intersection;
        this.recalculationPeriod = recalculationPeriod;
        this.operationRange = operationRange;
        nextRecalculationTime = 0;
        nonConflictConnectors = intersection.findNonConflictConnectors();
        stateRecalculationAlgorithm = new StateRecalculationAlgorithm(intersection.getName(), nonConflictConnectors);
        simulationTime = 0;
    }


    @Override
    public void updateInnerState(SimulationState simulationState, MessagingQueue messagingQueue) {
        simulationTime = simulationState.getSimulationTime();
        this.messagingQueue = messagingQueue;
        processMessages(messagingQueue);

        if (stateRecalculationAlgorithm.isRecalculationStarted()
                && simulationState.getSimulationTime() + 1e-4 >= recalculationCollectionTime) {
            stateRecalculationAlgorithm.recalculateState();
            IntersectionState state = getState();
            int sectionId = stateRecalculationAlgorithm.getSection() != null ? stateRecalculationAlgorithm.getSection().getId() : -1;
            double duration = stateRecalculationAlgorithm.getAverageSectionDuration();
            messagingQueue.addMessage(new AutonomousIntersectionStateMessage(intersection.getName(), state, sectionId, duration));
        }

        if (simulationState.getSimulationTime() + 1e-4 >= nextRecalculationTime) {
            messagingQueue.addMessage(new RequestVehicleDirections(intersection.getName()));
            stateRecalculationAlgorithm.startRecalculation();
            recalculationCollectionTime = simulationState.getSimulationTime() + 0.3;
            nextRecalculationTime += recalculationPeriod;
        }
    }

    private void processMessages(MessagingQueue messagingQueue) {
        for (ControlMessage message : messagingQueue.getCurrentMessages()) {
            if (!message.isBroadcast() && message.getReceiver().equals(intersection.getName())) {
                switch (message.getType()) {
                    case AUTONOMOUS_RESPONSE_DIRECTIONS:
                        if (!stateRecalculationAlgorithm.isRecalculationStarted()) {
                            continue;
                        }
                        ResponseVehicleDirection directionResponse = (ResponseVehicleDirection) message;
                        if (directionResponse.getPosition().distanceTo(intersection.getBasePoint()) > operationRange) {
                            continue;
                        }
                        stateRecalculationAlgorithm.addVehicleDescription(directionResponse.getSender(),
                                directionResponse.getPosition().distanceTo(intersection.getBasePoint()),
                                directionResponse.getConnectorDescription(), directionResponse.getWaitingTime());
                        break;
                    case AUTONOMOUS_INTERSECTION_PASSED:
                        if (stateRecalculationAlgorithm.vehiclePassed(message.getSender(), simulationTime)) {
                            IntersectionState state = getState();
                            int sectionId = stateRecalculationAlgorithm.getSection() != null ? stateRecalculationAlgorithm.getSection().getId() : -1;
                            double duration = stateRecalculationAlgorithm.getAverageSectionDuration();
                            messagingQueue.addMessage(new AutonomousIntersectionStateMessage(intersection.getName(), state, sectionId, duration));
                        }
                        break;
                    case AUTONOMOUS_REQUEST_PREFFERED_LANES:
                        messagingQueue.addMessage(responseToPrefferedLanesRequst((RequestPrefferedLanes) message));
                        break;
                    default:
                        throw new RuntimeException("Not supported message type");
                }
            }
        }
    }

    private ResponsePrefferedLanes responseToPrefferedLanesRequst(RequestPrefferedLanes request) {
        double[] priorities = new double[request.getRoadFrom().getNumLanes()];
        double min = Double.MAX_VALUE;
        HashMap<Integer, Double> data = new HashMap<>();
        for (int laneFrom = 0; laneFrom < request.getRoadFrom().getNumLanes(); laneFrom++) {
            int laneTo = Math.min(laneFrom, request.getRoadTo().getNumLanes());
            priorities[laneFrom] = nonConflictConnectors.get(new RoadConnectorDescription(
                    request.getRoadFrom(), laneFrom,
                    request.getRoadTo(), laneTo
            )).size();
            if (min > priorities[laneFrom]) {
                min = priorities[laneFrom];
            }
        }
        /*
        int maxIndex = 0;
        for (int i = 1; i < priorities.length; i++) {
            if (priorities[i] > priorities[maxIndex]) {
                maxIndex = i;
            }
        }
        */
        //TODO change this logic to more generic
        int x = (int) Math.round(priorities[0]);
        priorities[0] = priorities[1] = priorities[2] = 0;
        switch (x) {
            case 96:
                priorities[2] = 1;
                break;
            case 75:
            case 80:
                priorities[0] = 1;
                break;
            case 72:
                priorities[0] = 0;
                priorities[1] = 1.0;
                priorities[2] = 0.0;
                break;
            default:
                System.out.println(x);
                throw new RuntimeException("!");
        }
        for (int i = 0; i < priorities.length; i++) {
            data.put(i, priorities[i]);
        }
        /*
        double sum = 0;
        for (int i = 0; i < priorities.length; i++) {
            priorities[i] -= min - 1;
            sum += priorities[i];
        }
        for (int i = 0; i < priorities.length; i++) {
            data.put(i, priorities[i] / sum);
        }
        */
        return new ResponsePrefferedLanes(request.getReceiver(), request.getSender(), data);
    }

    @Override
    public IntersectionState getState() {
        return stateRecalculationAlgorithm.getState(simulationTime, messagingQueue);
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
