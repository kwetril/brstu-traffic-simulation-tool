package by.brstu.tst.core.simulation;

import by.brstu.tst.core.simulation.control.IntersectionController;
import by.brstu.tst.core.simulation.flows.IVehicleFlow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class SimulationConfig {
    private float timeStep;
    private List<IVehicleFlow> vehicleFlows;
    private List<IntersectionController> intersectionControllers;

    public SimulationConfig() {
        vehicleFlows = new ArrayList<>();
        intersectionControllers = new ArrayList<>();
    }

    public void setTimeStep(float timeStep) {
        this.timeStep = timeStep;
    }

    public void addFlow(IVehicleFlow flow) {
        vehicleFlows.add(flow);
    }

    public void addIntersectionController(IntersectionController controller) {
        intersectionControllers.add(controller);
    }

    public float getTimeStep() {
        return timeStep;
    }

    public List<IVehicleFlow> getVehicleFlows() {
        return vehicleFlows;
    }

    public List<IntersectionController> getIntersectionControllers() {
        return intersectionControllers;
    }
}
