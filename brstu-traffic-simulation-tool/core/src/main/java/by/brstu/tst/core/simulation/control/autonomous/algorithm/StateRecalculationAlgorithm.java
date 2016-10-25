package by.brstu.tst.core.simulation.control.autonomous.algorithm;

import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;
import by.brstu.tst.core.simulation.messaging.autonomous.AutonomousIntersectionCommand;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by a.klimovich on 23.10.2016.
 */
public class StateRecalculationAlgorithm {
    private Intersection intersection;
    private List<VehicleDescription> vehicles;
    private int numStatesToCalculate;
    private boolean recalculationStarted;
    private List<HashSet<String>> vehiclesPerState;

    public StateRecalculationAlgorithm(Intersection intersection, int numStatesToCalculate) {
        vehicles = new ArrayList<>();
        this.intersection = intersection;
        this.numStatesToCalculate = numStatesToCalculate;
        this.recalculationStarted = false;
    }

    public void addVehicleDescription(String id, double distance, RoadConnectorDescription connectorDescription) {
        vehicles.add(new VehicleDescription(id, distance, connectorDescription));
    }

    public IntersectionState getState() {
        return new IntersectionState(new HashSet<>());
    }

    public void recalculateState() {
        recalculationStarted = false;
    }

    public void generateControlMessages(SimulationState simulationState, MessagingQueue messagingQueue) {
        for (VehicleDescription vehicle : vehicles) {
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
