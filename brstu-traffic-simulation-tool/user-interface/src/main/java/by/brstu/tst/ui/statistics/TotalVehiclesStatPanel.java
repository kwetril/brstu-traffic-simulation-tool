package by.brstu.tst.ui.statistics;

import by.brstu.tst.core.statistics.TotalVehiclesStatCollector;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Created by a.klimovich on 13.10.2016.
 */
public class TotalVehiclesStatPanel extends JFrame {
    private XYSeriesCollection dataset;
    private HashMap<String, XYSeries> nameToSeriesMap;

    public TotalVehiclesStatPanel(String title, String xAxisLabel, String yAxisLabel,
                           String[] seriesNames, TotalVehiclesStatCollector collector) {
        dataset = new XYSeriesCollection();
        nameToSeriesMap = new HashMap<>();
        for (String seriesName : seriesNames) {
            XYSeries series = new XYSeries(seriesName);
            nameToSeriesMap.put(seriesName, series);
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                title, xAxisLabel, yAxisLabel,
                dataset, PlotOrientation.VERTICAL,
                true, true, false
        );
        ChartPanel panel = new ChartPanel(chart);

        collector.addNewPointListener((x, y, seriesName) -> {
            if (nameToSeriesMap.containsKey(seriesName)) {
                nameToSeriesMap.get(seriesName).add(x, y, true);
            }
        });

        panel.setPreferredSize(new Dimension(600, 400));
        setContentPane(panel);
        pack();
    }
}
