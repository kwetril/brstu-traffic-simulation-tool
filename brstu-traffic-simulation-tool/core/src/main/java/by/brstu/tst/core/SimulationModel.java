package by.brstu.tst.core;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.simulation.*;
import by.brstu.tst.core.simulation.flows.IVehicleFlow;
import by.brstu.tst.core.statistics.IStatsCollector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kwetril on 7/7/16.
 */
public class SimulationModel {
    private Map map;
    private SimulationConfig simulationConfig;
    private List<MovingVehicle> vehicles;
    private List<IStatsCollector> statsCollectors;
    private SimulationState state;

    public SimulationModel(Map map, SimulationConfig simulationConfig) {
        this.map = map;
        this.simulationConfig = simulationConfig;
        vehicles = new ArrayList<>();
        state = new SimulationState(map, vehicles, simulationConfig.getIntersectionControllers());
        statsCollectors = new ArrayList<>();
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
        UpdateVehicleStateVisitor vehicleStateUpdater = new UpdateVehicleStateVisitor(state,
                simulationConfig.getTimeStep());
        visitVehicles(vehicleStateUpdater);
        //remove vehicles which came to destination
        removeVehiclesReachedDestination();

        for (IStatsCollector statsCollector : statsCollectors) {
            statsCollector.updateStats(state);
        }

        //update model time
        state.updateSimulationTime(simulationConfig.getTimeStep());
    }

    public synchronized void visitVehicles(IVehicleVisitor visitor) {
        for (MovingVehicle vehicle : vehicles) {
            vehicle.accept(visitor);
        }
    }

    private void addGeneratedVehicles() {
        for (IVehicleFlow flow : simulationConfig.getVehicleFlows()) {
            flow.appendNewVehicles(state.getSimulationTime(), vehicles);
        }
    }

    private void removeVehiclesReachedDestination() {
        Iterator<MovingVehicle> vehicleIterator = vehicles.iterator();
        while (vehicleIterator.hasNext()) {
            MovingVehicle vehicle = vehicleIterator.next();
            if (vehicle.getRouteStateInfo().reachedDestination()) {
                vehicleIterator.remove();
            }
        }
    }

    public void addStatsCollector(IStatsCollector statsCollector) {
        statsCollectors.add(statsCollector);
    }
}
