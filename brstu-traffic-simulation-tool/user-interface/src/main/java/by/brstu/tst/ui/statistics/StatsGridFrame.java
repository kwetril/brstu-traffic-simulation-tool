package by.brstu.tst.ui.statistics;

import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by a.klimovich on 14.10.2016.
 */
public class StatsGridFrame extends JFrame {
    public StatsGridFrame(String title, int rows, int columns, ChartPanel[] statsPanels) {
        setTitle(title);
        GridLayout layout = new GridLayout(rows, columns);
        this.setLayout(layout);
        for (ChartPanel panel : statsPanels) {
            this.add(panel);
        }
        setPreferredSize(new Dimension(1200, 800));
        pack();
    }
}
