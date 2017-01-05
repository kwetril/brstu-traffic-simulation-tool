package by.brstu.tst.experiment;

/**
 * Created by a.klimovich on 06.01.2017.
 */
public class ExperimentResult {
    private double x;
    private double y;
    public ExperimentResult(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("%f\t%f", x, y);
    }
}
