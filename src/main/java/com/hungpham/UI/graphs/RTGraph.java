package com.hungpham.UI.graphs;

import com.hungpham.Utils.Utils;
import javafx.animation.AnimationTimer;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

/**
 * Creates real-time charts using JavaFX
 */
public abstract class RTGraph {
    protected static final int MAX_DATA_POINTS = 100;

    protected Series series;
    protected int xSeriesData = 0;
    protected ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<>();
    protected ExecutorService executor;
    protected NumberAxis xAxis;
    protected NumberAxis yAxis;
    protected Utils utils;

    protected LineChart<Number, Number> lineChart;

    /**
     * initialize chart
     * @param nameChart name for chart
     * @param lowerRange set the lowest y value for the chart
     * @param upperRange set the highest y value for the chart
     * @param tickUnit how far y values are seperated
     */
    protected void init(String nameChart, double lowerRange, double upperRange, double tickUnit) {
        utils = new Utils();
        xAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);

        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);

        yAxis = new NumberAxis(lowerRange, upperRange, tickUnit);
        //yAxis.setAutoRanging(true);

        //-- Chart
        lineChart = new LineChart<Number, Number>(xAxis, yAxis) {
            // Override to remove symbols on each data point
            @Override
            protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
            }
        };

        lineChart.setAnimated(false);
        lineChart.setId("SSNSLiveChart");
        lineChart.setTitle(nameChart);
        lineChart.setPrefSize(700, 450);

        //-- Chart Series
        series = new LineChart.Series<Number, Number>();
        series.setName(nameChart);
        lineChart.getData().add(series);
    }

    /**
     *
     * @return LineChart
     */
    public LineChart<Number, Number> getLineChart() {
        return lineChart;
    }

    /**
     * Timeline gets called in the JavaFX thread
     */
    protected void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }


    /**
     * update graph with new x and y values
     */
    protected void addDataToSeries() {
        for (int i = 0; i < MAX_DATA_POINTS; i++) { //-- add 20 numbers to the plot+
            if (dataQ.isEmpty()) break;
            if (xSeriesData > 1000) xSeriesData = 0;
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

    /**
     * add new x and y values to graph object
     */
    public abstract void executeGraph();
}