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
import by.brstu.tst.core.simulation.messaging.MessagingQueue;

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
    double laneWidth;

    public AutonomousIntersectionController(Intersection intersection) {
        this.intersection = intersection;
        allConnectors = new ArrayList<>();
        laneWidth = Double.MAX_VALUE;
        findNonConflictConnectors();
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
                BezierCurve connector = intersection.getConnector(allConnectors.get(i));
                BezierCurve anotherConnector = intersection.getConnector(allConnectors.get(j));
                double distance = connector.getDistance(anotherConnector);
                boolean connectorsConflict = distance < laneWidth * 0.8;
                if (!connectorsConflict) {
                    nonConflictConnectors.get(allConnectors.get(i)).add(allConnectors.get(j));
                    nonConflictConnectors.get(allConnectors.get(j)).add(allConnectors.get(i));
                }
            }
        }
    }

    private List<RoadConnectorDescription> getBestCombiination(List<WeightedConnectorDescription> weightedConnections) {
        return null;
    }

    @Override
    public void updateInnerState(SimulationState simulationState, MessagingQueue messagingQueue) {

    }

    @Override
    public IntersectionState getState() {
        return null;
    }

    @Override
    public Intersection getIntersection() {
        return null;
    }

    @Override
    public boolean connectorExist(DirectedRoad fromRoad, int fromLane, DirectedRoad toRoad) {
        return false;
    }

    @Override
    public void accept(IControllerVisitor visitor) {
        visitor.visit(this);
    }
}
