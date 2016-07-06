package by.brstu.tst.visualization;

/**
 * Created by kwetril on 7/6/16.
 */
public class TrafficSimulationToolGUI {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            SimulationMapFrame frame = new SimulationMapFrame(Map.GetTestMap());
            frame.setVisible(true);
        });
    }
}
