package by.brstu.tst.ui.statistics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by a.klimovich on 12.10.2016.
 */
public class ChartGrid extends JFrame {
    public ChartGrid() {
        GridLayout layout = new GridLayout(2, 2);
        this.setLayout(layout);
        this.add(createPanel("Panel 1"));
        this.add(createPanel("Panel 2"));
        this.add(createPanel("Panel 3"));
        this.add(createPanel("Panel 4"));
        setPreferredSize(new Dimension(1200, 800));
        pack();
    }

    private ChartPanel createPanel(String title) {
        XYDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset, title);
        return new ChartPanel(chart);
    }

    private JFreeChart createChart(XYDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                title, "X", "Y",
                dataset, PlotOrientation.VERTICAL,
                true, true, false
        );
        return chart;
    }

    private XYDataset createDataset() {
        XYSeries series1 = new XYSeries("Key1");
        XYSeries series2 = new XYSeries("Key2");
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            series1.add(i, random.nextDouble());
            series2.add(i, random.nextDouble());
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
    }
}
