package by.brstu.tst.ui.simulation;

import by.brstu.tst.core.SimulationModel;
import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.simulation.SimulationConfig;
import by.brstu.tst.core.statistics.SpeedStatsCollector;
import by.brstu.tst.core.statistics.TotalVehiclesStatCollector;
import by.brstu.tst.io.xml.MapReader;
import by.brstu.tst.io.xml.SimulationConfigReader;
import by.brstu.tst.ui.statistics.*;

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
    private SpeedStatsPanel speedStatsPanel;
    private TotalVehiclesStatPanel totalVehiclesStatPanel;
    private Map map;

    public SimulationFrame() {
        setTitle("Traffic simulation tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        simulationPanel = new SimulationPanel(this);
        add(simulationPanel, BorderLayout.CENTER);
        //pack();

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
                speedStatsPanel.setVisible(false);
                speedStatsPanel = null;
                totalVehiclesStatPanel.setVisible(false);
                totalVehiclesStatPanel = null;
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
        JMenuItem chartExample = new JMenuItem("Chart example");
        chartExample.addActionListener(actionEvent -> {
            JFrame chartFrame = new ChartExample();
            chartFrame.setVisible(true);
        });
        statisticsMenu.add(chartExample);
        JMenuItem timeSeriesExample = new JMenuItem("Time series example");

        timeSeriesExample.addActionListener(actionEvent -> {
            JFrame timeSeriesFrame = new TimeSeriesPlotExample();
            timeSeriesFrame.setVisible(true);
        });
        statisticsMenu.add(timeSeriesExample);
        JMenuItem histogramExample = new JMenuItem("Histogram example");

        histogramExample.addActionListener(actionEvent -> {
            JFrame histogramFrame = new HistogramExample();
            histogramFrame.setVisible(true);
        });
        statisticsMenu.add(histogramExample);

        JMenuItem xyPlotExample = new JMenuItem("XY plot example");
        xyPlotExample.addActionListener(actionEvent -> {
            JFrame xyPlotFrame = new PlotXYExample();
            xyPlotFrame.setVisible(true);
        });
        statisticsMenu.add(xyPlotExample);

        JMenuItem chartGridExample = new JMenuItem("Chart grid example");
        chartGridExample.addActionListener(actionEvent -> {
            JFrame chartGridFrame = new ChartGrid();
            chartGridFrame.setVisible(true);
        });
        statisticsMenu.add(chartGridExample);

        JMenuItem speedStats = new JMenuItem("Speed stats");
        speedStats.addActionListener(actionEvent -> {
            speedStatsPanel.setVisible(true);
        });
        statisticsMenu.add(speedStats);

        JMenuItem totalVehiclesStat = new JMenuItem("Total vehicles stats");
        totalVehiclesStat.addActionListener(actionEvent -> {
            totalVehiclesStatPanel.setVisible(true);
        });
        statisticsMenu.add(totalVehiclesStat);

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
                    "io-utils\\data\\simulation2.xml");
            SimulationModel simulationModel = new SimulationModel(map, simulationConfig);
            SpeedStatsCollector speedStatsCollector = new SpeedStatsCollector(1.0);
            simulationModel.addStatsCollector(speedStatsCollector);
            TotalVehiclesStatCollector totalVehiclesStatCollector = new TotalVehiclesStatCollector(1.0);
            simulationModel.addStatsCollector(totalVehiclesStatCollector);
            speedStatsPanel = new SpeedStatsPanel("Speed of vehicles in system",
                    "Simulation time", "Speed, m/s",
                    new String[] {"min", "25%", "50%", "75%", "max"}, speedStatsCollector);
            totalVehiclesStatPanel = new TotalVehiclesStatPanel("Total number of vehicles",
                    "Simulation time", "Number of vehicles", new String[] {"total"},
                    totalVehiclesStatCollector);
            simulationPanel.setupSimulation(simulationModel);

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
