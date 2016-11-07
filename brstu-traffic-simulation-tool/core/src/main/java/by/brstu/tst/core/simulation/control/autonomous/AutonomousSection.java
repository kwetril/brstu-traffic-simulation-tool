package by.brstu.tst.core.simulation.control.autonomous;

import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.control.IntersectionState;

import java.util.HashMap;
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

    public static void decreaseId() {
        nextId--;
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

    public int getNumVehicles() {
        return vehicles.size();
    }

    public static IntersectionState buildIntermediateState(AutonomousSection currentSection,
                                                           AutonomousSection nextSection,
                                                           HashMap<RoadConnectorDescription, HashSet<RoadConnectorDescription>> nonConflictConnectors) {
        HashSet<RoadConnectorDescription> currentConnectors = currentSection.getState().getOpenedConnections();
        HashSet<RoadConnectorDescription> nextConnectors = nextSection.getState().getOpenedConnections();
        HashSet<RoadConnectorDescription> intermediateConnectors = new HashSet<>();
        for (RoadConnectorDescription currentConnector : currentConnectors) {
            boolean hasConflicts = false;
            for (RoadConnectorDescription nextConnector : nextConnectors) {
                if (!nonConflictConnectors.get(currentConnector).contains(nextConnector) && !currentConnector.equals(nextConnector)) {
                    hasConflicts = true;
                    break;
                }
            }
            if (!hasConflicts) {
                intermediateConnectors.add(currentConnector);
            }
        }
        for (RoadConnectorDescription nextConnector : nextConnectors) {
            boolean hasConflicts = false;
            for (RoadConnectorDescription currentConnector : currentConnectors) {
                if (!nonConflictConnectors.get(nextConnector).contains(currentConnector) && !nextConnector.equals(currentConnector)) {
                    hasConflicts = true;
                    break;
                }
            }
            if (!hasConflicts) {
                intermediateConnectors.add(nextConnector);
            }
        }
        return new IntersectionState(intermediateConnectors);
    }
}
