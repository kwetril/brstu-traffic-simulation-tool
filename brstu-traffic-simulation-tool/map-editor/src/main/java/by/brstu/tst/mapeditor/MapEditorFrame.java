package by.brstu.tst.mapeditor;

import by.brstu.tst.core.ModelState;
import by.brstu.tst.core.config.Configuration;
import by.brstu.tst.core.config.ConfigurationReader;
import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.MapBuilder;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.vehicle.Vehicle;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by kwetril on 7/11/16.
 */
public class MapEditorFrame extends JFrame {

    private MapEditorPanel editorPanel;
    private JLabel statusLabel;
    private JMenuItem startSimulationMenuItem;
    private boolean simulationStarted;

    public MapEditorFrame() {
        setTitle("Traffic simulation tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        editorPanel = new MapEditorPanel(this);
        add(editorPanel, BorderLayout.CENTER);
        add(createStatusPanel(), BorderLayout.SOUTH);
        JMenuBar menuBar = createMenu();
        setJMenuBar(menuBar);

        simulationStarted = false;
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
            if (simulationStarted) {
                stopSimulation();
                simulationStarted = false;
                startSimulationMenuItem.setText("Start");
            } else {
                startSimulation();
                simulationStarted = true;
                startSimulationMenuItem.setText("Stop");
            }
        });
        simulationMenu.add(startSimulationMenuItem);
        menuBar.add(simulationMenu);

        return menuBar;
    }

    private void startSimulation() {
        editorPanel.startSimulation();
    }

    private void stopSimulation() {
        editorPanel.stopSimulation();
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    private void openMap() {
        ConfigurationReader configReader = new ConfigurationReader();
        Configuration config = configReader.read();
        MapBuilder mapBuilder = new MapBuilder(config);
        mapBuilder
                .addSourceElement("from-west", new MapPoint(2637887.23f, 6815777.55f))
                .addDestinationElement("to-east", new MapPoint(2639344.31f, 6816231.39f))
                .addSourceElement("from-east", new MapPoint(2639339.53f, 6816255.28f))
                .addDestinationElement("to-west", new MapPoint(2637884.84f, 6815799.04f))
                .addSourceElement("from-north", new MapPoint(2638374.52f, 6816670.9f))
                .addDestinationElement("to-south", new MapPoint(2638737.59f, 6815357.14f))
                .addSourceElement("from-south", new MapPoint(2638787.75f, 6815369.08f))
                .addDestinationElement("to-north", new MapPoint(2638417.51f, 6816673.29f))
                .addIntersection("intersection", new MapPoint(2638579.94f, 6815994.91f));

        mapBuilder
                .addRoad("rd-1", "from-west", null,
                        "intersection", new MapPoint(2638544.11f, 6815972.22f), 3)
                .addRoad("rd-2", "from-east", null,
                        "intersection", new MapPoint(2638613.38f, 6816011.63f), 3)
                .addRoad("rd-3", "from-north", null,
                        "intersection", new MapPoint(2638551.28f, 6816005.66f), 3)
                .addRoad("rd-4", "from-south", null,
                        "intersection", new MapPoint(2638605.02f, 6815978.19f), 3)
                .addRoad("rd-5", "intersection", new MapPoint(2638618.16f, 6815992.52f),
                        "to-east", null, 3)
                .addRoad("rd-6", "intersection", new MapPoint(2638534.56f, 6815992.52f),
                        "to-west", null, 3)
                .addRoad("rd-7", "intersection", new MapPoint(2638563.22f, 6815963.86f),
                        "to-south", null, 3)
                .addRoad("rd-8", "intersection", new MapPoint(2638589.5f, 6816018.8f),
                        "to-north", null, 3);

        Map map = mapBuilder.build("map-1");

        ArrayList<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Vehicle(map.getNode("from-west").getBasePoint(),
                new Vector(map.getNode("from-west").getBasePoint(),
                        map.getNode("to-east").getBasePoint()).setLength(10)));
        ModelState modelState = new ModelState(map, vehicles);

        editorPanel.showMap(map, modelState);
        startSimulationMenuItem.setEnabled(true);
    }
}
