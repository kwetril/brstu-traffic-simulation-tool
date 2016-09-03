package by.brstu.tst.ui;

import by.brstu.tst.ui.simulation.SimulationFrame;

/**
 * Created by kwetril on 7/6/16.
 */
public class TrafficSimulationToolGUI {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            SimulationFrame frame = new SimulationFrame();
            frame.setVisible(true);
        });
    }
}
