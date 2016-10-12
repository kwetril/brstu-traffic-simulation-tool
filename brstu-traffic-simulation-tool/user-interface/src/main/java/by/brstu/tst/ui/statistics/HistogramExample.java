package by.brstu.tst.ui.statistics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.awt.*;

/**
 * Created by a.klimovich on 12.10.2016.
 */
public class HistogramExample extends JFrame {
    public HistogramExample() {
        HistogramDataset dataset = createDataset();

        JFreeChart chart = createChart(dataset, "title");

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(600, 400));
        setContentPane(panel);
        pack();
    }

    private JFreeChart createChart(HistogramDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createHistogram(title, "X", "Y", dataset,
                PlotOrientation.VERTICAL, true, true, false);
        return chart;
    }

    private HistogramDataset createDataset() {
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Key",
                new double[]{1, 1, 2, 2, 1, 3, 5, 3, 2, 4, 7, 3, 3, 3, 4, 3, 5}, 5);
        return dataset;
    }
}
