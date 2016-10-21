package by.brstu.tst.ui.simulation.status;

import by.brstu.tst.core.simulation.MovingVehicle;
import org.jfree.ui.tabbedui.VerticalLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class VehicleInfoPanel extends JPanel {
    private JLabel idLabel;
    private JLabel speedLabel;
    private JLabel accelerationLabel;

    public VehicleInfoPanel() {
        setLayout(new VerticalLayout());
        idLabel = new JLabel("Id");
        idLabel.setPreferredSize(new Dimension(200, idLabel.getMinimumSize().height));
        add(idLabel);
        speedLabel = new JLabel("Speed");
        add(speedLabel);
        accelerationLabel = new JLabel("Acceleration");
        add(accelerationLabel);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    public void setVehicleSelector(VehicleSelector vehicleSelector) {
        vehicleSelector.addListener(vehicle -> {
            idLabel.setText("Id: " + vehicle.getVehicleInfo().getIdentifier());
            speedLabel.setText("Speed: " + vehicle.getSpeed());
            accelerationLabel.setText("Acceleration: " + vehicle.getAcceletation());
        });
    }
}
