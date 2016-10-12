package by.brstu.tst.ui.statistics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeTableXYDataset;
import org.joda.time.DateTime;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by a.klimovich on 12.10.2016.
 */
public class TimeSeriesPlotExample extends JFrame {
    public TimeSeriesPlotExample() {
        TimeTableXYDataset dataset = createDataset();

        JFreeChart chart = createChart(dataset, "title");

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(600, 400));
        setContentPane(panel);
        pack();
    }

    private JFreeChart createChart(TimeTableXYDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title,
                "Simulation time",
                "Random value",
                dataset,
                true,
                true,
                false
        );
        return chart;
    }

    private TimeTableXYDataset createDataset() {
        TimeTableXYDataset dataset = new TimeTableXYDataset();
        DateTime dateTime = new DateTime();

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            dataset.add(new Second(i % 60, i / 60, dateTime.getHourOfDay(),
                    dateTime.getDayOfMonth(), dateTime.getMonthOfYear(),
                    dateTime.getYear()), random.nextDouble(), "First");
            dataset.add(new Second(i % 60, i / 60, dateTime.getHourOfDay(),
                    dateTime.getDayOfMonth(), dateTime.getMonthOfYear(),
                    dateTime.getYear()), random.nextDouble(), "Second");
        }
        return dataset;
    }
}
