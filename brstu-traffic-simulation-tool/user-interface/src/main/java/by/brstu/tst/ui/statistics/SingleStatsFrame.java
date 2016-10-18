package by.brstu.tst.ui.statistics;

import org.jfree.chart.ChartPanel;

import javax.swing.*;

/**
 * Created by a.klimovich on 14.10.2016.
 */
public class SingleStatsFrame extends JFrame {
    public SingleStatsFrame(String title, ChartPanel statsPanel) {
        setTitle(title);
        setContentPane(statsPanel);
        pack();
    }
}
