package by.brstu.tst.mapeditor;

import by.brstu.tst.core.vehicle.IVehicleVisitor;
import by.brstu.tst.core.vehicle.Vehicle;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by a.klimovich on 03.09.2016.
 */
public class VehicleDrawVisitor implements IVehicleVisitor {
    private Graphics2D graphics;

    public VehicleDrawVisitor(Graphics2D graphics) {
        this.graphics = graphics;
    }

    @Override
    public void visit(Vehicle vehicle) {
        graphics.setColor(Color.RED);
        Shape vehicleImage = new Ellipse2D.Float(vehicle.getPosition().getX(), vehicle.getPosition().getY(),
                3, 4);
        graphics.fill(vehicleImage);
    }
}
