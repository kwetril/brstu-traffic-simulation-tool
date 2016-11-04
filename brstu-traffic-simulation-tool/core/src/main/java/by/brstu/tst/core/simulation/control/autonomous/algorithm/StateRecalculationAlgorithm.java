package by.brstu.tst.core.simulation.control.autonomous.algorithm;

import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.control.autonomous.AutonomousSection;
import by.brstu.tst.core.simulation.control.autonomous.WeightedSectionPart;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;
import by.brstu.tst.core.simulation.messaging.autonomous.AutonomousIntersectionCommand;

import java.util.*;

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

    public AutonomousSection getSection() {
        return sectionQueue.getCurrentSection();
    }

    public double getAverageSectionDuration() {
        return 20.0;
    }

    public void recalculateState(MessagingQueue messageQueue) {
        while (sectionQueue.getSize() > 3 && sectionQueue.getLastSection().getNumVehicles() < 40) {
            sectionQueue.removeLastSection();
        }
        boolean wereUpdates = true;
        while (wereUpdates) {
            wereUpdates = false;
            List<WeightedSectionPart> weightedConnectors = new ArrayList<>();
            for (Map.Entry<String, ArrayList<VehicleDescription>> descriptionsByDirection : vehiclesByDirections.entrySet()) {
                ArrayList<VehicleDescription> vehicleDescriptions = descriptionsByDirection.getValue();
                Collections.sort(vehicleDescriptions, (x, y) -> Double.compare(x.getDistance(), y.getDistance()));
                ArrayList<RoadConnectorDescription> connectorDescriptions = new ArrayList<>();
                ArrayList<String> vehicles = new ArrayList<>();
                int maxVehicles = 15;
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
                    wereUpdates = true;
                    while (i < vehicleDescriptions.size()
                            && vehicles.size() < maxVehicles
                            && connectorDescriptions.contains(vehicleDescriptions.get(i).getConnectorDescription())) {
                        if (sectionQueue.vehicleRegistered(vehicleDescriptions.get(i).getId())) {
                            i++;
                            continue;
                        }
                        vehicles.add(vehicleDescriptions.get(i).getId());
                        weight += 10 + Math.exp(-0.01 * vehicleDescriptions.get(i).getDistance());
                        weight += Math.exp(0.1 * vehicleDescriptions.get(i).getWaitingTime()) - 1;
                        i++;
                    }
                    weightedConnectors.add(new WeightedSectionPart(connectorDescriptions, weight, nonConflictConnectors, vehicles));
                }
            }
            if (weightedConnectors.size() == 0) {
                break;
            }
            WeightedGraph graph = new WeightedGraph(weightedConnectors);
            List<WeightedSectionPart> optimalParts = graph.getOptimalSectionParts();
            AutonomousSection section = weightedSectionPartsToSection(optimalParts);
            int numVehicles = 0;
            for (WeightedSectionPart part : optimalParts) {
                for (String vehicle : part.getVehicles()) {
                    numVehicles++;
                    messageQueue.addMessage(new AutonomousIntersectionCommand(intersectionName,
                            vehicle, section.getId()));
                }
            }
            System.out.printf("Section %s: %s\n", section.getId(), numVehicles);
            sectionQueue.addSection(section);
        }
        for (Map.Entry<String, ArrayList<VehicleDescription>> descriptionsByDirection : vehiclesByDirections.entrySet()) {
            for (VehicleDescription vehicle : descriptionsByDirection.getValue()) {
                if (!sectionQueue.vehicleRegistered(vehicle.getId())) {
                    messageQueue.addMessage(new AutonomousIntersectionCommand(intersectionName,
                            vehicle.getId(), sectionQueue.getSize() + 1));
                }
            }
        }
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
