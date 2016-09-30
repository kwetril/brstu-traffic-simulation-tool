package by.brstu.tst.ui.simulation;

import by.brstu.tst.core.SimulationModel;
import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.simulation.distribution.ExponentialDistribution;
import by.brstu.tst.core.simulation.distribution.IRandomDistribution;
import by.brstu.tst.core.simulation.flows.ActivationPeriod;
import by.brstu.tst.core.simulation.flows.IVehicleFlow;
import by.brstu.tst.core.simulation.routing.Route;
import by.brstu.tst.core.simulation.flows.StaticVehicleFlow;
import by.brstu.tst.core.vehicle.VehicleType;
import by.brstu.tst.io.xml.MapReader;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Created by kwetril on 7/5/16.
 */
public class SimulationFrame extends JFrame {

    private SimulationPanel simulationPanel;
    private JLabel statusLabel;
    private JMenuItem startSimulationMenuItem;

    public SimulationFrame() {
        setTitle("Traffic simulation tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        simulationPanel = new SimulationPanel(this);
        add(simulationPanel, BorderLayout.CENTER);
        pack();

        add(createStatusPanel(), BorderLayout.SOUTH);
        JMenuBar menuBar = createMenu();
        setJMenuBar(menuBar);

        pack();
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel("Status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        return statusPanel;
    }

    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openMapMenuItem = new JMenuItem("Open");
        openMapMenuItem.addActionListener(actionEvent -> {
            openMap();
        });
        fileMenu.add(openMapMenuItem);
        menuBar.add(fileMenu);

        JMenu simulationMenu = new JMenu("Simulation");
        startSimulationMenuItem = new JMenuItem("Start");
        startSimulationMenuItem.setEnabled(false);
        startSimulationMenuItem.addActionListener(actionEvent -> {
            if (simulationPanel.isSimulationStarted()) {
                simulationPanel.stopSimulation();
                startSimulationMenuItem.setText("Start");
            } else {
                simulationPanel.startSimulation();
                startSimulationMenuItem.setText("Stop");
            }
        });
        simulationMenu.add(startSimulationMenuItem);
        menuBar.add(simulationMenu);

        return menuBar;
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    private void openMap() {
        MapReader mapReader = new MapReader();
        Map map = mapReader.readMap("io-utils\\data\\map.xml");

        SimulationModel modelState = new SimulationModel(map, 0.1f);

        ActivationPeriod activationPeriod = new ActivationPeriod();
        Route route = new Route(map, new String[] {
            "from-west", "rd-1", "intersection", "rd-5", "to-east"
        });
        IRandomDistribution flowDistribution = new ExponentialDistribution(0.1f, 123);
        IVehicleFlow vehicleFlow = new StaticVehicleFlow(VehicleType.CAR, route,
                flowDistribution, activationPeriod);
        modelState.addVehicleFlow(vehicleFlow);

        simulationPanel.showMap(map, modelState);
        startSimulationMenuItem.setEnabled(true);
    }
}
