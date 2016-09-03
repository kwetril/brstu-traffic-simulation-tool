package by.brstu.tst.core;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.elements.BaseRoadElementVisitor;
import by.brstu.tst.core.vehicle.IVehicleVisitor;
import by.brstu.tst.core.vehicle.Vehicle;

import java.util.List;

/**
 * Created by kwetril on 7/7/16.
 */
public class ModelState {
    private Map map;
    private List<Vehicle> vehicles;

    public ModelState(Map map, List<Vehicle> vehicles) {
        this.map = map;
        this.vehicles = vehicles;
    }

    public void visitVehicles(IVehicleVisitor visitor) {
        for (Vehicle vehicle : vehicles) {
            vehicle.accept(visitor);
        }
    }

    public void visitModel(BaseRoadElementVisitor roadElementVisitor, IVehicleVisitor vehicleVisitor) {
        map.visitElements(roadElementVisitor);
        visitVehicles(vehicleVisitor);
    }
}
