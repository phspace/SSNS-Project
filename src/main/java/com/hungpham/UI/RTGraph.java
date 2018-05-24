package com.hungpham.UI;

import com.hungpham.Utils.Utils;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

/**
 * Creates a real-time chart using SwingWorker
 */
public abstract class RTGraph {
    protected static final int MAX_DATA_POINTS = 100;

    protected Series series;
    protected int xSeriesData = 0;
    protected ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<Number>();
    protected ExecutorService executor;
    protected NumberAxis xAxis;
    protected Utils utils;

    protected final LineChart<Number, Number> lineChart;

    public RTGraph() {
        utils = new Utils();
        xAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);

        //-- Chart
        lineChart = new LineChart<Number, Number>(xAxis, yAxis) {
            // Override to remove symbols on each data point
            @Override
            protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
            }
        };
    }

    protected void init(String nameChart) {
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        lineChart.setAnimated(false);
        lineChart.setId("SSNSLiveChart");
        lineChart.setTitle(nameChart);
        lineChart.setPrefSize(640, 400);

        //-- Chart Series
        series = new LineChart.Series<Number, Number>();
        series.setName(nameChart);
        lineChart.getData().add(series);
    }

    public LineChart<Number, Number> getLineChart() {
        return lineChart;
    }

    //-- Timeline gets called in the JavaFX thread
    protected void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    protected void addDataToSeries() {
        for (int i = 0; i < MAX_DATA_POINTS; i++) { //-- add 20 numbers to the plot+
            if (dataQ.isEmpty()) break;
            series.getData().add(new LineChart.Data(xSeriesData++, dataQ.remove()));
        }
        // remove points to keep us at no more than MAX_DATA_POINTS
        if (series.getData().size() > MAX_DATA_POINTS) {
            series.getData().remove(0, series.getData().size() - MAX_DATA_POINTS);
        }
        // update
        xAxis.setLowerBound(xSeriesData - MAX_DATA_POINTS);
        xAxis.setUpperBound(xSeriesData - 1);
    }

    public abstract void executeGraph();
}