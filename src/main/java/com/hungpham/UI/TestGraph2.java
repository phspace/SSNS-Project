package com.hungpham.UI;

import com.hungpham.Controller.Definitions;
import com.hungpham.Main;
import com.hungpham.Utils.Utils;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A chart that fills in the area between a line of data points and the axes.
 * Good for comparing accumulated totals over time.
 *
 * @related charts/line/LineChart
 * @related charts/scatter/ScatterChart
 * @see javafx.scene.chart.Chart
 * @see javafx.scene.chart.Axis
 * @see javafx.scene.chart.NumberAxis
 */
public class TestGraph2 extends Application {
    private static final int MAX_DATA_POINTS = 100;

    private Series series;
    private int xSeriesData = 0;
    private ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<Number>();
    private ExecutorService executor;
    private AddToQueue addToQueue;
    private Timeline timeline2;
    private NumberAxis xAxis;
    private Utils utils;

    private void init(Stage primaryStage) {
        utils = new Utils();
        xAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);

        //-- Chart
        final LineChart<Number, Number> sc = new LineChart<Number, Number>(xAxis, yAxis) {
            // Override to remove symbols on each data point
            @Override
            protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
            }
        };
        sc.setAnimated(false);
        sc.setId("liveAreaChart");
        sc.setTitle("Animated Area Chart");

        //-- Chart Series
        series = new AreaChart.Series<Number, Number>();
        series.setName("Area Chart Series");
        sc.getData().add(series);

        primaryStage.setScene(new Scene(sc));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();

        //-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        addToQueue = new AddToQueue();
        executor.execute(addToQueue);
        //-- Prepare Timeline
        prepareTimeline();
    }

    public static void main(String[] args) {
        Main.startEverything();
        launch(args);
    }

    private class AddToQueue implements Runnable {
        public void run() {
            // add a item of random data to queue
            String accz = utils.TCPReceive(Definitions.GRAPH_PORT);
            dataQ.add(Double.parseDouble(accz));
            executor.execute(this);
        }
    }

    //-- Timeline gets called in the JavaFX Main thread
    private void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    private void addDataToSeries() {
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
}