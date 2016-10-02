package by.brstu.tst.core;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.simulation.IVehicleVisitor;
import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.SimulationConfig;
import by.brstu.tst.core.simulation.UpdateVehicleStateVisitor;
import by.brstu.tst.core.simulation.flows.IVehicleFlow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kwetril on 7/7/16.
 */
public class SimulationModel {
    private Map map;
    private float simulationTime;
    private SimulationConfig simulationConfig;
    private List<MovingVehicle> vehicles;

    public SimulationModel(Map map, SimulationConfig simulationConfig) {
        this.map = map;
        this.simulationConfig = simulationConfig;
        this.simulationTime = 0;
        this.vehicles = new ArrayList<>();
    }

    public synchronized void performSimulationSteps(int numSteps) {
        for (int i = 0; i < numSteps; i++) {
            doSimulationStep();
        }
    }

    private void doSimulationStep() {
        //generate new vehicles
        addGeneratedVehicles();
        //update vehicles positions and states
        UpdateVehicleStateVisitor vehicleStateUpdater = new UpdateVehicleStateVisitor(simulationConfig.getTimeStep());
        visitVehicles(vehicleStateUpdater);
        //remove vehicles which came to destination
        removeVehiclesReachedDestination();

        //update model time
        simulationTime += simulationConfig.getTimeStep();
    }

    public synchronized void visitVehicles(IVehicleVisitor visitor) {
        for (MovingVehicle vehicle : vehicles) {
            vehicle.accept(visitor);
        }
    }

    private void addGeneratedVehicles() {
        for (IVehicleFlow flow : simulationConfig.getVehicleFlows()) {
            flow.appendNewVehicles(simulationTime, vehicles);
        }
    }

    private void removeVehiclesReachedDestination() {
        Iterator<MovingVehicle> vehicleIterator = vehicles.iterator();
        while (vehicleIterator.hasNext()) {
            MovingVehicle vehicle = vehicleIterator.next();
            if (vehicle.reachedDestination()) {
                vehicleIterator.remove();
            }
        }
    }
}
