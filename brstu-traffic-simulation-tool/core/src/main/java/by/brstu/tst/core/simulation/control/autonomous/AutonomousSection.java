package by.brstu.tst.core.simulation.control.autonomous;

import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.control.IntersectionState;

import java.util.HashSet;

/**
 * Created by a.klimovich on 30.10.2016.
 */
public class AutonomousSection {
    private static int nextId = 0;
    private int id;
    private HashSet<String> vehicles;
    private IntersectionState state;

    public AutonomousSection(IntersectionState state, Iterable<String> vehicles) {
        this.id = nextId;
        nextId++;
        this.state = state;
        this.vehicles = new HashSet<>();
        for (String vehicle : vehicles) {
            this.vehicles.add(vehicle);
        }
    }

    public int getId() {
        return id;
    }

    public IntersectionState getState() {
        return state;
    }

    public void vehiclePassed(String vehicleId) {
        vehicles.remove(vehicleId);
    }

    public boolean allPassed() {
        return vehicles.isEmpty();
    }

    public HashSet<String> getVehicles() {
        return vehicles;
    }
}
