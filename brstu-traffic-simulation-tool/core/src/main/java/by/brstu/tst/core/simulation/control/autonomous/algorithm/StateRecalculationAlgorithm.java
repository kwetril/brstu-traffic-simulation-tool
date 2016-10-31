package by.brstu.tst.core.simulation.control.autonomous.algorithm;

import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.control.autonomous.AutonomousSection;
import by.brstu.tst.core.simulation.control.autonomous.WeightedSectionPart;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;
import by.brstu.tst.core.simulation.messaging.autonomous.AutonomousIntersectionCommand;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by a.klimovich on 23.10.2016.
 */
public class StateRecalculationAlgorithm {
    private String intersectionName;
    private HashMap<String, ArrayList<VehicleDescription>> vehiclesByDirections;
    private boolean recalculationStarted;
    private HashMap<RoadConnectorDescription, HashSet<RoadConnectorDescription>> nonConflictConnectors;
    private SectionQueue sectionQueue;

    public StateRecalculationAlgorithm(String intersectionName, int numStatesToCalculate,
                                       HashMap<RoadConnectorDescription, HashSet<RoadConnectorDescription>> nonConflictConnectors) {
        this.intersectionName = intersectionName;
        this.recalculationStarted = false;
        this.nonConflictConnectors = nonConflictConnectors;
        vehiclesByDirections = new HashMap<>();
        sectionQueue = new SectionQueue();
    }

    public void addVehicleDescription(String id, double distance, RoadConnectorDescription connectorDescription,
                                      double waitingTime) {
        String key = String.format("%s %s", connectorDescription.getFrom(), connectorDescription.getFromLane());
        if (!vehiclesByDirections.containsKey(key)) {
            vehiclesByDirections.put(key, new ArrayList<>());
        }
        vehiclesByDirections.get(key).add(new VehicleDescription(id, distance, connectorDescription, waitingTime));
    }

    public IntersectionState getState() {
        sectionQueue.updateState();
        return sectionQueue.getCurrentState();
    }

    public void recalculateState() {
        List<WeightedSectionPart> weightedConnectors = new ArrayList<>();
        for (Map.Entry<String, ArrayList<VehicleDescription>> descriptionsByDirection : vehiclesByDirections.entrySet()) {
            ArrayList<VehicleDescription> vehicleDescriptions = descriptionsByDirection.getValue();
            Collections.sort(vehicleDescriptions, (x, y) -> Double.compare(x.getDistance(), y.getDistance()));
            ArrayList<RoadConnectorDescription> connectorDescriptions = new ArrayList<>();
            ArrayList<String> vehicles = new ArrayList<>();
            int maxVehicles = 5;
            int i = 0;
            double weight = 0;
            for (int k = 0; k < 3 && i < vehicleDescriptions.size() && vehicles.size() < maxVehicles; k++) {
                while (i < vehicleDescriptions.size() && sectionQueue.vehicleRegistered(vehicleDescriptions.get(i).getId())) {
                    i++;
                }
                if (i >= vehicleDescriptions.size()) {
                    break;
                }
                connectorDescriptions.add(vehicleDescriptions.get(i).getConnectorDescription());
                while (i < vehicleDescriptions.size()
                        && vehicles.size() < maxVehicles
                        && connectorDescriptions.contains(vehicleDescriptions.get(i).getConnectorDescription())) {
                    if (sectionQueue.vehicleRegistered(vehicleDescriptions.get(i).getId())) {
                        i++;
                        continue;
                    }
                    vehicles.add(vehicleDescriptions.get(i).getId());
                    weight += Math.exp(-0.01 * vehicleDescriptions.get(i).getDistance());
                    weight += Math.exp(0.1 * vehicleDescriptions.get(i).getWaitingTime()) - 1;
                    i++;
                }
                weightedConnectors.add(new WeightedSectionPart(connectorDescriptions, weight, nonConflictConnectors, vehicles));
            }
        }
        WeightedGraph graph = new WeightedGraph(weightedConnectors);
        AutonomousSection section = weightedSectionPartsToSection(graph.getOptimalSectionParts());
        sectionQueue.addSection(section);
        vehiclesByDirections.clear();
        recalculationStarted = false;
    }

    private AutonomousSection weightedSectionPartsToSection(List<WeightedSectionPart> parts) {
        List<String> vehicles = new ArrayList<>();
        HashSet<RoadConnectorDescription> connectors = new HashSet<>();
        for (WeightedSectionPart part : parts) {
            for (String vehicle : part.getVehicles()) {
                vehicles.add(vehicle);
            }
            connectors.addAll(part.getConnectorDescriptions());
        }
        IntersectionState state = new IntersectionState(connectors);
        return new AutonomousSection(state, vehicles);
    }

    public void generateControlMessages(MessagingQueue messagingQueue) {
        for (VehicleDescription vehicle : vehiclesByDirections.entrySet()
                .stream()
                .map(x -> x.getValue())
                .flatMap(x -> x.stream())
                .collect(Collectors.toList())) {
            messagingQueue.addMessage(new AutonomousIntersectionCommand(intersectionName, vehicle.getId()));
        }
    }

    public void startRecalculation() {
        recalculationStarted = true;
    }

    public boolean isRecalculationStarted() {
        return recalculationStarted;
    }

    public boolean vehiclePassed(String sender) {
        return sectionQueue.vehiclePassed(sender);
    }
}
