package by.brstu.tst.experiment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by a.klimovich on 05.01.2017.
 */
public class ExperimentSetuper {
    private int numSteps;
    String mapPath;
    File tmpConfigFile;

    public ExperimentSetuper(ExperimentConfig config) throws IOException {
        numSteps = (int) Math.round(Math.ceil(config.getSimulationTime() / config.getTimeStep()));
        mapPath = getPathToResource("/" + config.getMapPath() + ".xml");
        String numLanes = config.getMapPath().split("-")[1];

        String mainTemplate = getResourceContent("/config-template.txt");
        mainTemplate = mainTemplate.replace("#time-step#", "" + config.getTimeStep());
        mainTemplate = mainTemplate.replace("#type#", config.getControlAlgorithm());

        String algoConfigContent;
        if (config.getControlAlgorithm().equals("autonomous")) {
            algoConfigContent = getResourceContent("/autonomous-config-part.txt");
        } else {
            algoConfigContent = getResourceContent("/" + config.getControlAlgorithm() + "-config-" + numLanes + "-lane-part.txt");
        }

        String flowTemplate = getResourceContent("/flow-template-" + config.getFlowPattern() + ".txt");
        flowTemplate = flowTemplate.replace("#double-flow#", "" + config.getBaseFlow() * 2);
        flowTemplate = flowTemplate.replace("#base-flow#", "" + config.getBaseFlow());
        flowTemplate = flowTemplate.replace("#half-flow#", "" + config.getBaseFlow() / 2);

        mainTemplate = mainTemplate.replace("#flows#", flowTemplate);
        mainTemplate = mainTemplate.replace("#controllers#", algoConfigContent);

        tmpConfigFile = createTempFile(mainTemplate);
    }

    private String getPathToResource(String resource) {
        return this.getClass().getResource(resource).getPath().replaceFirst("^/(.:/)", "$1");
    }

    private String getResourceContent(String resource) throws IOException {
        String pathToResource = getPathToResource(resource);
        String content = String.join("\n", Files.readAllLines(Paths.get(pathToResource)));
        return content;
    }

    private File createTempFile(String content) throws IOException {
        File temp = File.createTempFile("tempfile", ".tmp");
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
        bw.write(content);
        bw.close();
        return temp;
    }

    public String getMapPath() {
        return mapPath;
    }

    public String getConfigPath() {
        return tmpConfigFile.getAbsolutePath();
    }

    public void cleanup() {
        tmpConfigFile.delete();
    }

    public int getSteps() {
        return numSteps;
    }
}
