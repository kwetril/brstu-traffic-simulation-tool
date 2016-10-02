package by.brstu.tst.core.simulation;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.simulation.control.IntersectionController;

import java.util.List;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class SimulationState {
    private Map map;
    private List<MovingVehicle> vehicles;

    private List<IntersectionController> controllers;
    private float simulationTime;

    public SimulationState(Map map, List<MovingVehicle> vehicles, List<IntersectionController> controllers) {
        this.map = map;
        this.vehicles = vehicles;
        this.controllers = controllers;
        simulationTime = 0;
    }

    public List<MovingVehicle> getVehicles() {
        return vehicles;
    }

    public float getSimulationTime() {
        return simulationTime;
    }

    public void updateSimulationTime(float timeDelta) {
        simulationTime += timeDelta;
    }

    public IntersectionController getController(Intersection intersection) {
        for (IntersectionController controller : controllers) {
            if (controller.getIntersection().getName().equals(intersection.getName())) {
                return controller;
            }
        }
        return null;
    }
}
