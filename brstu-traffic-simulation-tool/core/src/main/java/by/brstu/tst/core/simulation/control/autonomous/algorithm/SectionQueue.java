package by.brstu.tst.core.simulation.control.autonomous.algorithm;

import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.control.autonomous.AutonomousSection;

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

    SectionQueue() {
        sectionQueue = new ArrayDeque<>();
        vehicleToSection = new HashMap<>();
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

    void updateState() {
        while (!sectionQueue.isEmpty() && sectionQueue.peek().allPassed()) {
            sectionQueue.remove();
        }
    }

    boolean vehiclePassed(String vehicleId) {
        if (vehicleToSection.containsKey(vehicleId)) {
            AutonomousSection section = vehicleToSection.get(vehicleId);
            section.vehiclePassed(vehicleId);
            vehicleToSection.remove(vehicleId);
            if (section.allPassed()) {
                return true;
            }
        }
        return false;
    }

    boolean vehicleRegistered(String id) {
        return vehicleToSection.containsKey(id);
    }

    IntersectionState getCurrentState() {
        if (sectionQueue.isEmpty()) {
            return IntersectionState.EMPTY;
        }
        return sectionQueue.peek().getState();
    }

    AutonomousSection getCurrentSection() {
        return sectionQueue.peek();
    }

    int getSize() {
        return sectionQueue.size();
    }
}
