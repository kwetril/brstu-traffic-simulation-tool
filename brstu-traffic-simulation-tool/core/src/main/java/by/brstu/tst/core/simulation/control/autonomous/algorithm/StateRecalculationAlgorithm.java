package by.brstu.tst.core.simulation.control.autonomous.algorithm;

import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.control.autonomous.WeightedSectionPart;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;
import by.brstu.tst.core.simulation.messaging.autonomous.AutonomousIntersectionCommand;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by a.klimovich on 23.10.2016.
 */
public class StateRecalculationAlgorithm {
    private Intersection intersection;
    private HashMap<String, ArrayList<VehicleDescription>> vehiclesByDirections;
    private int numStatesToCalculate;
    private boolean recalculationStarted;
    private List<HashSet<String>> vehiclesPerState;
    private HashMap<RoadConnectorDescription, HashSet<RoadConnectorDescription>> nonConflictConnectors;
    private IntersectionState intersectionState;

    public StateRecalculationAlgorithm(Intersection intersection, int numStatesToCalculate,
                                       HashMap<RoadConnectorDescription, HashSet<RoadConnectorDescription>> nonConflictConnectors) {
        this.intersection = intersection;
        this.numStatesToCalculate = numStatesToCalculate;
        this.recalculationStarted = false;
        this.nonConflictConnectors = nonConflictConnectors;
        intersectionState = new IntersectionState(new HashSet<>());
        vehiclesByDirections = new HashMap<>();
    }

    public void addVehicleDescription(String id, double distance, RoadConnectorDescription connectorDescription) {
        String key = String.format("%s %s", connectorDescription.getFrom(), connectorDescription.getFromLane());
        if (!vehiclesByDirections.containsKey(key)) {
            vehiclesByDirections.put(key, new ArrayList<>());
        }
        vehiclesByDirections.get(key).add(new VehicleDescription(id, distance, connectorDescription));
    }

    public IntersectionState getState() {
        return intersectionState;
    }

    public void recalculateState() {
        List<WeightedSectionPart> weightedConnectors = new ArrayList<>();
        for (Map.Entry<String, ArrayList<VehicleDescription>> descriptionsByDirection : vehiclesByDirections.entrySet()) {
            Collections.sort(descriptionsByDirection.getValue(), (x, y) -> Double.compare(x.getDistance(), y.getDistance()));
            RoadConnectorDescription connectorDescription = descriptionsByDirection.getValue().get(0).getConnectorDescription();
            int i = 0;
            double weight = 0;
            while (i < descriptionsByDirection.getValue().size()
                    && descriptionsByDirection.getValue().get(i).getConnectorDescription().equals(connectorDescription)) {
                weight += Math.exp(-0.01 * descriptionsByDirection.getValue().get(i).getDistance());
                i++;
            }
            weightedConnectors.add(new WeightedSectionPart(connectorDescription, weight, nonConflictConnectors));
        }
        WeightedGraph graph = new WeightedGraph(weightedConnectors);
        intersectionState = new IntersectionState(new HashSet<>(graph.getBestConnectors()));
        vehiclesByDirections.clear();
        recalculationStarted = false;
    }

    public void generateControlMessages(SimulationState simulationState, MessagingQueue messagingQueue) {
        for (VehicleDescription vehicle : vehiclesByDirections.entrySet()
                .stream()
                .map(x -> x.getValue())
                .flatMap(x -> x.stream())
                .collect(Collectors.toList())) {
            messagingQueue.addMessage(new AutonomousIntersectionCommand(intersection.getName(), vehicle.getId()));
        }
    }

    public void startRecalculation() {
        recalculationStarted = true;
    }

    public boolean isRecalculationStarted() {
        return recalculationStarted;
    }

    public void vehiclePassed(String sender) {
        for (HashSet<String> vehicles : vehiclesPerState) {
            if (vehiclesPerState.remove(sender)) {
                return;
            }
        }
    }
}
