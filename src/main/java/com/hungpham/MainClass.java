package com.hungpham;

import com.hungpham.UI.AcceGraph;
import com.hungpham.UI.BaroGraph;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class MainClass extends Application {
    public static volatile String command = "";

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Button stopButton = new Button("Stop Everything!");
        stopButton.setMinSize(200,100);

        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                command = "stop";
            }
        });
        AcceGraph acceGraph = new AcceGraph();
        BaroGraph baroGraph = new BaroGraph();
        LineChart<Number, Number> lc = acceGraph.getLineChart();
        LineChart<Number, Number> lc1 = baroGraph.getLineChart();

        FlowPane root = new FlowPane();
        root.getChildren().addAll(lc, lc1, stopButton);

        Scene scene = new Scene(root, 1280, 900);

        primaryStage.setTitle("SSNS Project");
        primaryStage.setScene(scene);
        primaryStage.show();
        acceGraph.executeGraph();
        baroGraph.executeGraph();
    }

    public static void main(String[] args) {
        FunctionsWrapper.startEverything();
        launch(args);
    }
}
