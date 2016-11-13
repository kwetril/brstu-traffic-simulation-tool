package by.brstu.tst.core.simulation.control.autonomous;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.control.IControllerVisitor;
import by.brstu.tst.core.simulation.control.IntersectionController;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.control.autonomous.algorithm.LaneChoosingAlgorithm;
import by.brstu.tst.core.simulation.control.autonomous.algorithm.StateRecalculationAlgorithm;
import by.brstu.tst.core.simulation.control.autonomous.algorithm.VehiclePerDirectionStats;
import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;
import by.brstu.tst.core.simulation.messaging.autonomous.*;
import by.brstu.tst.core.simulation.messaging.BroadcastIntersectionStateMessage;
import com.sun.org.apache.xpath.internal.SourceTree;

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

    private LaneChoosingAlgorithm laneChoosingAlgorithm;

    public AutonomousIntersectionController(Intersection intersection, double recalculationPeriod,
                                            double operationRange) {
        this.intersection = intersection;
        this.recalculationPeriod = recalculationPeriod;
        this.operationRange = operationRange;
        nextRecalculationTime = 0;
        nonConflictConnectors = intersection.findNonConflictConnectors();
        stateRecalculationAlgorithm = new StateRecalculationAlgorithm(intersection.getName(), nonConflictConnectors);
        simulationTime = 0;

        laneChoosingAlgorithm = new LaneChoosingAlgorithm(intersection);
    }


    @Override
    public void updateInnerState(SimulationState simulationState, MessagingQueue messagingQueue) {
        simulationTime = simulationState.getSimulationTime();
        this.messagingQueue = messagingQueue;
        processMessages(messagingQueue);

        if (stateRecalculationAlgorithm.isRecalculationStarted()
                && simulationState.getSimulationTime() + 1e-4 >= recalculationCollectionTime) {
            stateRecalculationAlgorithm.recalculateState();
            messagingQueue.addMessage(new BroadcastIntersectionStateMessage(intersection.getName(), getState()));
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
                        laneChoosingAlgorithm.removeVehicle(message.getSender());
                        if (stateRecalculationAlgorithm.vehiclePassed(message.getSender(), simulationTime)) {
                            messagingQueue.addMessage(new BroadcastIntersectionStateMessage(intersection.getName(), getState()));
                        }
                        break;
                    case AUTONOMOUS_REQUEST_PREFFERED_LANES:
                        RequestPrefferedLanes requestPrefferedLanes = (RequestPrefferedLanes) message;
                        laneChoosingAlgorithm.addVehicle(requestPrefferedLanes.getSender(),
                                requestPrefferedLanes.getRoadFrom(), requestPrefferedLanes.getRoadTo());
                        messagingQueue.addMessage(responseToPrefferedLanesRequst(requestPrefferedLanes));
                        break;
                    default:
                        throw new RuntimeException("Not supported message type");
                }
            }
        }
    }



    private ResponsePrefferedLanes responseToPrefferedLanesRequst(RequestPrefferedLanes request) {
        HashMap<Integer, Double> data = new HashMap<>();
        double[] priorities = laneChoosingAlgorithm.getLanePriorities(request.getRoadFrom(), request.getRoadTo());
        for (int i = 0; i < priorities.length; i++) {
            data.put(i, priorities[i]);
        }
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
