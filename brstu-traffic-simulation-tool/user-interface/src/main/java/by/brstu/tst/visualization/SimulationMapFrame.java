package by.brstu.tst.visualization;

import javax.swing.*;
import java.awt.*;

/**
 * Created by kwetril on 7/5/16.
 */
public class SimulationMapFrame extends JFrame {
    public SimulationMapFrame(Map map) {
        setTitle("Traffic simulation tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        SimulationMapPanel mapPanel = new SimulationMapPanel(map);
        add(mapPanel, BorderLayout.CENTER);
        pack();
    }


}
