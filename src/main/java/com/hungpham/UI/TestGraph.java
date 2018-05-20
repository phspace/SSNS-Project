package com.hungpham.UI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicInteger;

public class TestGraph extends Application {

    private AtomicInteger tick = new AtomicInteger(0);

    @Override
    public void start(Stage stage) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setAnimated(false);
        xAxis.setLabel("Tick");

        yAxis.setAnimated(false);
        yAxis.setLabel("Value");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Values");

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.getData().add(series);

        Scene scene = new Scene(chart, 620, 350);
        stage.setScene(scene);
        stage.show();

        Thread updateThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);

                    Platform.runLater(() -> {
                        for (int i = 0; i < 20; i++) { //-- add 20 numbers to the plot+
                            series.getData().add(new XYChart.Data<>(tick.incrementAndGet(), (int) (Math.random() * 100)));
                        }
                        if (series.getData().size() > 20) {
                            series.getData().remove(0, series.getData().size() - 20);
                        }
                        xAxis.setLowerBound(tick.incrementAndGet() - 20);
                        xAxis.setUpperBound(tick.incrementAndGet() - 1);
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}