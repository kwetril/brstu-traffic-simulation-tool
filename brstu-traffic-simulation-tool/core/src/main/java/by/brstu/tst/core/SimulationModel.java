package by.brstu.tst.core;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.simulation.*;
import by.brstu.tst.core.simulation.control.IControllerVisitor;
import by.brstu.tst.core.simulation.control.IntersectionController;
import by.brstu.tst.core.simulation.flows.IVehicleFlow;
import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;
import by.brstu.tst.core.statistics.IStatsCollector;
import com.google.common.collect.Iterables;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kwetril on 7/7/16.
 */
public class SimulationModel {
    private Map map;
    private List<IVehicleFlow> vehicleFlows;
    private List<IntersectionController> intersectionControllers;
    private float timeStep;
    private List<MovingVehicle> vehicles;
    private List<IStatsCollector> statsCollectors;
    private SimulationState state;
    private MessagingQueue messagingQueue;

    public SimulationModel(Map map, List<IVehicleFlow> vehicleFlows,
                           List<IntersectionController> intersectionControllers,
                           float timeStep) {
        this.map = map;
        this.vehicleFlows = vehicleFlows;
        this.intersectionControllers = intersectionControllers;
        this.timeStep = timeStep;
        vehicles = new ArrayList<>();
        state = new SimulationState(map, vehicles);
        statsCollectors = new ArrayList<>();
        messagingQueue = new MessagingQueue();
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
                messagingQueue, timeStep);
        visitVehicles(vehicleStateUpdater);

        //collect statistics
        for (IStatsCollector statsCollector : statsCollectors) {
            statsCollector.updateStats(state);
        }

        //remove vehicles which came to destination
        removeVehiclesReachedDestination();

        //update state of controllers
        for (IntersectionController controller : intersectionControllers) {
            controller.updateInnerState(state, messagingQueue);
        }

        //update model time
        messagingQueue.endSimulationStep();
        state.updateSimulationTime(timeStep);
    }

    public synchronized void visitVehicles(IVehicleVisitor visitor) {
        for (MovingVehicle vehicle : vehicles) {
            vehicle.accept(visitor);
        }
    }

    public synchronized void visitControllers(IControllerVisitor visitor) {
        for (IntersectionController controller : intersectionControllers) {
            controller.accept(visitor);
        }
    }

    private void addGeneratedVehicles() {
        for (IVehicleFlow flow : vehicleFlows) {
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
