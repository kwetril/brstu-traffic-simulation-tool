package by.brstu.tst.core.simulation.control.autonomous.algorithm;

import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.control.autonomous.AutonomousSection;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

/**
 * Created by a.klimovich on 30.10.2016.
 */
public class SectionQueue {
    private Queue<AutonomousSection> sectionQueue;
    private HashMap<String, AutonomousSection> vehicleToSection;

    public SectionQueue() {
        sectionQueue = new ArrayDeque<>();
        vehicleToSection = new HashMap<>();
    }

    public void addSection(AutonomousSection section) {
        sectionQueue.add(section);
        for (String vehicle : section.getVehicles()) {
            vehicleToSection.put(vehicle, section);
        }
    }

    public void updateState() {
        while (!sectionQueue.isEmpty() && sectionQueue.peek().allPassed()) {
            sectionQueue.remove();
        }
    }

    public boolean vehiclePassed(String vehicleId) {
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

    public boolean vehicleRegistered(String id) {
        return vehicleToSection.containsKey(id);
    }

    public IntersectionState getCurrentState() {
        if (sectionQueue.isEmpty()) {
            return IntersectionState.EMPTY;
        }
        return sectionQueue.peek().getState();
    }
}
