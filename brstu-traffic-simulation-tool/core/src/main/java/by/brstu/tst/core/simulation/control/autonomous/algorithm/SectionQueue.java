package by.brstu.tst.core.simulation.control.autonomous.algorithm;

import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.control.autonomous.AutonomousSection;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;
import by.brstu.tst.core.simulation.messaging.autonomous.AutonomousIntersectionStateMessage;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

/**
 * Created by a.klimovich on 30.10.2016.
 */
class SectionQueue {
    private ArrayDeque<AutonomousSection> sectionQueue;
    private HashMap<String, AutonomousSection> vehicleToSection;
    private HashMap<RoadConnectorDescription, HashSet<RoadConnectorDescription>> nonConflictConnectors;

    private double intermediateSectionTime;
    private IntersectionState intermedateState;
    private double intermediateStateExpiration;
    private String intersectionName;

    SectionQueue(String intersectionName, double intermediateSectionTime,
                 HashMap<RoadConnectorDescription, HashSet<RoadConnectorDescription>> nonConflictConnectors) {
        sectionQueue = new ArrayDeque<>();
        vehicleToSection = new HashMap<>();
        this.intermediateSectionTime= intermediateSectionTime;
        this.nonConflictConnectors = nonConflictConnectors;
        this.intersectionName = intersectionName;
    }

    void addSection(AutonomousSection section) {
        sectionQueue.add(section);
        for (String vehicle : section.getVehicles()) {
            vehicleToSection.put(vehicle, section);
        }
    }

    AutonomousSection getLastSection() {
        return sectionQueue.getLast();
    }

    void removeLastSection() {
        HashSet<String> sectionVehicles = sectionQueue.getLast().getVehicles();
        for (String vehicle : sectionVehicles) {
            if (vehicleToSection.containsKey(vehicle)) {
                vehicleToSection.remove(vehicle);
            }
        }
        sectionQueue.removeLast();
        AutonomousSection.decreaseId();
    }

    boolean vehiclePassed(String vehicleId, double simulationTime) {
        if (vehicleToSection.containsKey(vehicleId)) {
            AutonomousSection section = vehicleToSection.get(vehicleId);
            section.vehiclePassed(vehicleId);
            vehicleToSection.remove(vehicleId);
            if (section.allPassed()) {
                while (!sectionQueue.isEmpty() && sectionQueue.peek().allPassed()) {
                    sectionQueue.remove();
                }
                if (!sectionQueue.isEmpty()) {
                    intermedateState = AutonomousSection.buildIntermediateState(section, sectionQueue.peek(), nonConflictConnectors);
                    intermediateStateExpiration = simulationTime + intermediateSectionTime;
                }
                return true;
            }
        }
        return false;
    }

    boolean vehicleRegistered(String id) {
        return vehicleToSection.containsKey(id);
    }

    IntersectionState getCurrentState(double simulationTime, MessagingQueue messagingQueue) {
        boolean intermediateStateFinished = false;
        if (intermedateState != null) {
            if (intermediateStateExpiration > simulationTime) {
                return intermedateState;
            }
            else {
                intermedateState = null;
                intermediateStateFinished = true;
            }
        }
        IntersectionState result;
        if (sectionQueue.isEmpty()) {
            result = IntersectionState.EMPTY;
        }
        else {
            result = sectionQueue.peek().getState();
        }
        if (intermediateStateFinished) {
            messagingQueue.addMessage(new AutonomousIntersectionStateMessage(intersectionName, result, 0, 0));
        }
        return result;
    }

    AutonomousSection getCurrentSection() {
        return sectionQueue.peek();
    }

    int getSize() {
        return sectionQueue.size();
    }
}
