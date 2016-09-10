package by.brstu.tst.core;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.UpdateVehicleStateVisitor;
import by.brstu.tst.core.simulation.flows.IVehicleFlow;
import by.brstu.tst.core.simulation.IVehicleVisitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kwetril on 7/7/16.
 */
public class SimulationModel {
    private Map map;
    private float timeStep;
    private float simulationTime;
    private IVehicleVisitor updateVehiclesVisitor;
    private List<MovingVehicle> vehicles;
    private List<IVehicleFlow> vehicleFlows;

    public SimulationModel(Map map, float initTimeStep) {
        this.map = map;
        this.timeStep = initTimeStep;
        this.simulationTime = 0;
        this.updateVehiclesVisitor = new UpdateVehicleStateVisitor(initTimeStep);
        this.vehicles = new ArrayList<>();
        this.vehicleFlows = new ArrayList<>();
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
        visitVehicles(updateVehiclesVisitor);
        //remove vehicles which came to destination
        removeVehiclesReachedDestination();

        //update model time
        simulationTime += timeStep;
    }

    public synchronized void visitVehicles(IVehicleVisitor visitor) {
        for (MovingVehicle vehicle : vehicles) {
            vehicle.accept(visitor);
        }
    }

    public void addVehicleFlow(IVehicleFlow vehicleFlow) {
        vehicleFlows.add(vehicleFlow);
    }

    private void addGeneratedVehicles() {
        for (IVehicleFlow flow : vehicleFlows) {
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
