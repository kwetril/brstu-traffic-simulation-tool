package by.brstu.tst.ui.simulation.status;

import by.brstu.tst.core.SimulationModel;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.ui.simulation.SimulationPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class VehicleSelector {
    private MovingVehicle selectedVehicle;
    private List<VehicleStateListener> listeners;

    public VehicleSelector(SimulationPanel panel, SimulationModel simulationModel) {
        listeners = new ArrayList<>();
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                Point2D coordinates = new Point2D.Float();
                panel.getTransformState().getTransform().transform(event.getPoint(), coordinates);
                try {
                    panel.getTransformState().getTransform().inverseTransform(event.getPoint(), coordinates);
                } catch (NoninvertibleTransformException e) {
                    e.printStackTrace();
                }
                MapPoint mapPoint = new MapPoint(coordinates.getX(), coordinates.getY());
                SelectedVehicleSearcher vehicleSearcher = new SelectedVehicleSearcher(mapPoint);
                simulationModel.visitVehicles(vehicleSearcher);
                selectedVehicle = vehicleSearcher.getSelectedVehicle();
                updateStatus();
            }
        });
    }

    public void addListener(VehicleStateListener listener) {
        listeners.add(listener);
    }

    public MovingVehicle getSelectedVehicle() {
        return selectedVehicle;
    }

    public void updateStatus() {
        if (selectedVehicle != null) {
            for (VehicleStateListener listener : listeners) {
                listener.selectedVehicleStatusChanged(selectedVehicle);
            }
        }
    }


    //catch clicks on map, find closest vehicles, select them
    //generate events
    //store selected vehicle event listeners
}
