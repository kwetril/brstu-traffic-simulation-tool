package by.brstu.tst.experiment;

import by.brstu.tst.core.SimulationModel;
import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.simulation.SimulationConfig;
import by.brstu.tst.core.statistics.TimeInSystemStatsCollector;
import by.brstu.tst.io.xml.MapReader;
import by.brstu.tst.io.xml.ParseException;
import by.brstu.tst.io.xml.SimulationConfigReader;
import org.joda.time.DateTime;

import java.io.IOException;
import java.io.PrintWriter;

public class ExperimentTool {
    public static ExperimentResult runEperiment(String mapPath, String configPath, int steps) throws IOException, ParseException {
        MapReader mapReader = new MapReader();
        Map map = mapReader.readMap(mapPath);

        SimulationConfigReader simulationConfigReader = new SimulationConfigReader(map);
        SimulationConfig simulationConfig = simulationConfigReader.readSimulationConfig(configPath);
        SimulationModel simulationModel = new SimulationModel(map, simulationConfig.getVehicleFlows(),
                simulationConfig.getIntersectionControllers(), simulationConfig.getTimeStep());
        TimeInSystemStatsCollector timeInSystemStatsCollector = new TimeInSystemStatsCollector(1.0);
        simulationModel.addStatsCollector(timeInSystemStatsCollector);
        ExperimentResultStatListener resultStatListener = new ExperimentResultStatListener();
        timeInSystemStatsCollector.addNewPointListener(resultStatListener);
        simulationModel.performSimulationSteps(steps);

        return new ExperimentResult(resultStatListener.getResultX(), resultStatListener.getResultY());
    }

    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter("experiments.txt", "UTF-8")) {
            double simulationStep = 0.1;
            double simulationTime = 3610;
            for (String mapName : new String[]{"map-2-lane", "map-3-lane"}) {
                //for (String mapName : new String[]{"map-2-lane"}) {
                for (String flowPattern : new String[]{"even", "uneven"}) {
                    //for (String flowPattern : new String[]{"even"}) {
                    for (String algo : new String[]{"cyclic", "adaptive", "autonomous"}) {
                        //for (String algo : new String[]{"adaptive"}) {
                        for (double baseLoad : new double[]{0.02, 0.04, 0.06, 0.08, 0.1, 0.12, 0.14, 0.16, 0.18, 0.2}) {
                            try {
                                ExperimentConfig config = new ExperimentConfig(simulationStep,
                                        baseLoad, simulationTime, flowPattern, algo, mapName);
                                writer.println(config.getExperimentShortName());
                                System.out.println(config.getExperimentShortName() + " " + new DateTime().toString("HH:mm:ss"));
                                ExperimentSetuper experimentSetuper = new ExperimentSetuper(config);
                                ExperimentResult result = runEperiment(experimentSetuper.getMapPath(), experimentSetuper.getConfigPath(), experimentSetuper.getSteps());
                                writer.println(result);
                                writer.flush();
                                experimentSetuper.cleanup();
                            } catch (Exception ex) {
                                System.out.println("Exception during experiment");
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception outer) {
            outer.printStackTrace();
        }
    }
}
