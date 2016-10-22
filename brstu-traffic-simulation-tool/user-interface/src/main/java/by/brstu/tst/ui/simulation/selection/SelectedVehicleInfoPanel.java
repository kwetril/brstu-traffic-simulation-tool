package by.brstu.tst.ui.simulation.selection;

import org.jfree.ui.tabbedui.VerticalLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class SelectedVehicleInfoPanel extends JPanel {
    private JLabel idLabel;
    private JLabel speedLabel;
    private JLabel accelerationLabel;
    private JLabel hasCarsInFrondLabel;

    public SelectedVehicleInfoPanel() {
        setLayout(new VerticalLayout());
        idLabel = new JLabel("Id: -");
        idLabel.setPreferredSize(new Dimension(200, idLabel.getMinimumSize().height));
        add(idLabel);
        speedLabel = new JLabel("Speed: -");
        add(speedLabel);
        accelerationLabel = new JLabel("Acceleration: -");
        add(accelerationLabel);
        hasCarsInFrondLabel = new JLabel("Has cars in front: -");
        add(hasCarsInFrondLabel);

        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5),
                "Selected vehicle info"));
        setBackground(Color.LIGHT_GRAY);
    }

    public void setVehicleSelector(VehicleSelector vehicleSelector) {
        vehicleSelector.addListener(vehicle -> {
            idLabel.setText(String.format("Id: %s", vehicle.getVehicleInfo().getIdentifier()));
            speedLabel.setText(String.format("Speed (m/s): %.3f", vehicle.getSpeed()));
            accelerationLabel.setText(String.format("Acceleration (m / s^2): %.3f", vehicle.getAcceletation()));
            hasCarsInFrondLabel.setText(String.format("Has cars in front: %s", vehicle.getDriver().seeCarsInFront()));
        });
    }
}
