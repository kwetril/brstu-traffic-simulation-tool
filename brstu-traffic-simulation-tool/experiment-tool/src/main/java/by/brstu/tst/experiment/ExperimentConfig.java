package by.brstu.tst.experiment;

public class ExperimentConfig {
    private double timeStep;
    private double baseFlow;
    private double simulationTime;
    private String flowPattern;
    private String controlAlgorithm;
    private String mapPath;

    public ExperimentConfig(double timeStep, double baseFlow, double simulationTime, String flowPattern, String controlAlgorithm, String mapPath) {
        this.timeStep = timeStep;
        this.baseFlow = baseFlow;
        this.simulationTime = simulationTime;
        this.flowPattern = flowPattern;
        this.controlAlgorithm = controlAlgorithm;
        this.mapPath = mapPath;
    }

    public double getTimeStep() {
        return timeStep;
    }

    public double getBaseFlow() {
        return baseFlow;
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public String getFlowPattern() {
        return flowPattern;
    }

    public String getControlAlgorithm() {
        return controlAlgorithm;
    }

    public String getMapPath() {
        return mapPath;
    }

    public String getExperimentShortName() {
        return String.format("%s-%s %s %f", mapPath, flowPattern, controlAlgorithm, baseFlow);
    }
}
