package by.brstu.tst.ui.simulation;

import by.brstu.tst.core.SimulationModel;
import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.simulation.SimulationConfig;
import by.brstu.tst.core.statistics.SpeedStatsCollector;
import by.brstu.tst.core.statistics.TimeInSystemStatsCollector;
import by.brstu.tst.core.statistics.TotalVehiclesStatCollector;
import by.brstu.tst.core.statistics.VehicleDynmicsStatCollector;
import by.brstu.tst.io.xml.MapReader;
import by.brstu.tst.io.xml.SimulationConfigReader;
import by.brstu.tst.ui.simulation.selection.SelectedVehicleInfoPanel;
import by.brstu.tst.ui.statistics.*;
import org.jfree.chart.ChartPanel;

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
    private JMenuItem pauseSimulationMenuItem;
    private StatsGridFrame statsGridFrame;
    private SelectedVehicleInfoPanel vehicleInfoPanel;
    private Map map;

    public SimulationFrame() {
        setTitle("Traffic simulation tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        simulationPanel = new SimulationPanel(this);
        add(simulationPanel, BorderLayout.CENTER);

        add(createStatusPanel(), BorderLayout.SOUTH);
        add(createVehicleStatusPanel(), BorderLayout.EAST);
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

    private JPanel createVehicleStatusPanel() {
        vehicleInfoPanel = new SelectedVehicleInfoPanel();
        return vehicleInfoPanel;
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
        simulationMenu.add(startSimulationMenuItem);
        pauseSimulationMenuItem = new JMenuItem("Pause");
        pauseSimulationMenuItem.setVisible(false);
        simulationMenu.add(pauseSimulationMenuItem);
        menuBar.add(simulationMenu);
        startSimulationMenuItem.addActionListener(actionEvent -> {
            if (simulationPanel.isSimulationStarted()) {
                simulationPanel.stopSimulation();
                startSimulationMenuItem.setText("Start");
                pauseSimulationMenuItem.setVisible(false);
                statsGridFrame.setVisible(false);
                statsGridFrame = null;
            } else {
                setupSimulation();
                simulationPanel.startSimulation();
                startSimulationMenuItem.setText("Stop");
                pauseSimulationMenuItem.setVisible(true);
            }
        });
        pauseSimulationMenuItem.addActionListener(actionEvent -> {
            if (simulationPanel.isSimulationStarted()) {
                simulationPanel.stopSimulation();
                pauseSimulationMenuItem.setText("Resume");
            } else {
                simulationPanel.startSimulation();
                pauseSimulationMenuItem.setText("Pause");
            }
        });

        JMenu statisticsMenu = new JMenu("Statistics");
        JMenuItem statsGridMenuItem = new JMenuItem("Stats grid");
        statsGridMenuItem.addActionListener(actionEvent -> statsGridFrame.setVisible(true));
        statisticsMenu.add(statsGridMenuItem);

        menuBar.add(statisticsMenu);

        return menuBar;
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    private void setupSimulation() {
        try {
            SimulationConfigReader simulationConfigReader = new SimulationConfigReader(map);
            SimulationConfig simulationConfig = simulationConfigReader.readSimulationConfig(
                    "io-utils\\data\\simulation3.xml");
            SimulationModel simulationModel = new SimulationModel(map, simulationConfig.getVehicleFlows(),
                    simulationConfig.getIntersectionControllers(), simulationConfig.getTimeStep());
            SpeedStatsCollector speedStatsCollector = new SpeedStatsCollector(1.0);
            simulationModel.addStatsCollector(speedStatsCollector);
            TotalVehiclesStatCollector totalVehiclesStatCollector = new TotalVehiclesStatCollector(1.0);
            simulationModel.addStatsCollector(totalVehiclesStatCollector);
            VehicleDynmicsStatCollector vehicleDynmicsStatCollector = new VehicleDynmicsStatCollector(1.0);
            simulationModel.addStatsCollector(vehicleDynmicsStatCollector);
            TimeInSystemStatsCollector timeInSystemStatsCollector = new TimeInSystemStatsCollector(1.0);
            simulationModel.addStatsCollector(timeInSystemStatsCollector);
            ChartPanel speedStatsPanel = ChartPanelHelper.createPanel("Speed of vehicles in system",
                    "Simulation time", "Speed, m/s",
                    new String[] {"min", "25%", "50%", "75%", "max"}, speedStatsCollector);
            ChartPanel totalVehiclesStatPanel = ChartPanelHelper.createPanel("Total number of vehicles",
                    "Simulation time", "Number of vehicles", new String[] {"total"},
                    totalVehiclesStatCollector);
            ChartPanel vehicleDynamicsStatsPanel = ChartPanelHelper.createPanel("Dynamics of vehicles in model",
                    "Simulation time", "Number of vehicles", new String[] {"added", "deleted", "throughput", "input"},
                    vehicleDynmicsStatCollector);
            ChartPanel timeInSystemStatsPanel = ChartPanelHelper.createPanel("Time in system",
                    "Simulation time", "Time in system",
                    new String[] {"min", "25%", "50%", "75%", "max"},
                    timeInSystemStatsCollector);
            statsGridFrame = new StatsGridFrame("Stats grid", 2, 2, new ChartPanel[] {
               speedStatsPanel, totalVehiclesStatPanel, vehicleDynamicsStatsPanel, timeInSystemStatsPanel
            });
            simulationPanel.setupSimulation(simulationModel);
            vehicleInfoPanel.setVehicleSelector(simulationPanel.getVehicleSelector());

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void openMap() {
        try {
            MapReader mapReader = new MapReader();
            map = mapReader.readMap("io-utils\\data\\map.xml");
            simulationPanel.showMap(map);
            startSimulationMenuItem.setEnabled(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
