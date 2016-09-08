package by.brstu.tst.core;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.elements.BaseRoadElementVisitor;
import by.brstu.tst.core.simulation.UpdateVehicleStateVisitor;
import by.brstu.tst.core.vehicle.IVehicleVisitor;
import by.brstu.tst.core.vehicle.Vehicle;

import java.util.List;

/**
 * Created by kwetril on 7/7/16.
 */
public class SimulationModel {
    private Map map;
    private List<Vehicle> vehicles;
    private float timeStep;
    private float simulationTime;
    private IVehicleVisitor updateVehiclesVisitor;

    public SimulationModel(Map map, List<Vehicle> vehicles, float initTimeStep) {
        this.map = map;
        this.vehicles = vehicles;
        this.simulationTime = 0;
        this.timeStep = initTimeStep;
        this.updateVehiclesVisitor = new UpdateVehicleStateVisitor(initTimeStep);
    }

    public void performSimulationSteps(int numSteps) {
        for (int i = 0; i < numSteps; i++) {
            doSimulationStep();
        }
    }

    private void doSimulationStep() {
        visitVehicles(updateVehiclesVisitor);
        simulationTime += timeStep;
    }

    public void visitVehicles(IVehicleVisitor visitor) {
        for (Vehicle vehicle : vehicles) {
            vehicle.accept(visitor);
        }
    }
}
