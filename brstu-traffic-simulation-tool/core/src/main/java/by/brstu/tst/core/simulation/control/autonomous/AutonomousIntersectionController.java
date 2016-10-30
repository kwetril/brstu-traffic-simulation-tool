package by.brstu.tst.core.simulation.control.autonomous;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.EdgeRoadElement;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.control.IControllerVisitor;
import by.brstu.tst.core.simulation.control.IntersectionController;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.control.autonomous.algorithm.StateRecalculationAlgorithm;
import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;
import by.brstu.tst.core.simulation.messaging.autonomous.AutonomousIntersectionStateMessage;
import by.brstu.tst.core.simulation.messaging.autonomous.RequestVehicleDirections;
import by.brstu.tst.core.simulation.messaging.autonomous.ResponseVehicleDirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class AutonomousIntersectionController implements IntersectionController {

    private Intersection intersection;
    private List<RoadConnectorDescription> allConnectors;
    private HashMap<RoadConnectorDescription, HashSet<RoadConnectorDescription>> nonConflictConnectors;
    private double laneWidth;
    private double nextRecalculationTime;
    private double recalculationCollectionTime;
    private double recalculationPeriod;
    private StateRecalculationAlgorithm stateRecalculationAlgorithm;

    public AutonomousIntersectionController(Intersection intersection, double recalculationPeriod,
                                            int numStatesToCalculate) {
        this.intersection = intersection;
        allConnectors = new ArrayList<>();
        laneWidth = Double.MAX_VALUE;
        this.recalculationPeriod = recalculationPeriod;
        nextRecalculationTime = 0;
        nonConflictConnectors = new HashMap<>();
        findNonConflictConnectors();
        stateRecalculationAlgorithm = new StateRecalculationAlgorithm(intersection, numStatesToCalculate, nonConflictConnectors);
    }

    private void findNonConflictConnectors() {
        for (EdgeRoadElement input : intersection.getInputElements()) {
            for (EdgeRoadElement output : intersection.getOutputElements()) {
                DirectedRoad inputRoad = input.getDirectedRoadByEndNode(intersection);
                DirectedRoad outputRoad = output.getDirectedRoadByStartNode(intersection);
                laneWidth = Math.min(laneWidth, Math.min(inputRoad.getLaneWidth(), outputRoad.getLaneWidth()));
                for (int inputLane = 0; inputLane < inputRoad.getNumLanes(); inputLane++) {
                    for (int outputLane = 0; outputLane < outputRoad.getNumLanes(); outputLane++) {
                        RoadConnectorDescription connector = new RoadConnectorDescription(inputRoad, inputLane,
                                outputRoad, outputLane);
                        allConnectors.add(connector);
                        nonConflictConnectors.put(connector, new HashSet<>());
                    }
                }
            }
        }
        for (int i = 0; i < allConnectors.size(); i++) {
            for (int j = i + 1; j < allConnectors.size(); j++) {
                RoadConnectorDescription first = allConnectors.get(i);
                RoadConnectorDescription second = allConnectors.get(j);
                if (first.getFrom().getName().equals(second.getFrom().getName())
                        && first.getFromLane() == second.getFromLane())  {
                    //non conflict when from the same lane
                    nonConflictConnectors.get(first).add(second);
                    nonConflictConnectors.get(second).add(first);
                    continue;
                }
                BezierCurve connector = intersection.getConnector(first);
                BezierCurve anotherConnector = intersection.getConnector(second);
                double distance = connector.getDistance(anotherConnector);
                boolean connectorsConflict = distance < laneWidth * 0.8;
                if (!connectorsConflict) {
                    nonConflictConnectors.get(first).add(second);
                    nonConflictConnectors.get(second).add(first);
                }
            }
        }
    }

    @Override
    public void updateInnerState(SimulationState simulationState, MessagingQueue messagingQueue) {
        processMessages(messagingQueue);

        if (stateRecalculationAlgorithm.isRecalculationStarted()
                && simulationState.getSimulationTime() + 1e-4 >= recalculationCollectionTime) {
            stateRecalculationAlgorithm.recalculateState();
            stateRecalculationAlgorithm.generateControlMessages(simulationState, messagingQueue);
            messagingQueue.addMessage(new AutonomousIntersectionStateMessage(intersection.getName(), getState()));
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
                        stateRecalculationAlgorithm.addVehicleDescription(directionResponse.getSender(),
                                directionResponse.getPosition().distanceTo(intersection.getBasePoint()),
                                directionResponse.getConnectorDescription());
                        break;
                    case AUTONOMOUS_INTERSECTION_PASSED:
                        stateRecalculationAlgorithm.vehiclePassed(message.getSender());
                        break;
                    default:
                        throw new RuntimeException("Not supported message type");
                }
            }
        }
    }

    @Override
    public IntersectionState getState() {
        return stateRecalculationAlgorithm.getState();
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
