package com.hungpham;

import com.hungpham.UI.AcceGraph;
import com.hungpham.UI.BaroGraph;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApplication extends Application {
    public static volatile String command = "";
    public static volatile int mode = 0;

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
        stopButton.setMinSize(200, 50);

        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                command = "stop";
            }
        });

        Button modeButton1 = new Button("1 Sensor");
        modeButton1.setMinSize(200, 50);

        modeButton1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                mode = 1;
            }
        });

        Button modeButton2 = new Button("2 Sensor");
        modeButton2.setMinSize(200, 50);

        modeButton2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                mode = 2;
            }
        });

        AcceGraph[] acceGraph = new AcceGraph[2];
        BaroGraph[] baroGraph = new BaroGraph[2];
        LineChart<Number, Number>[] lc = new LineChart[2];
        LineChart<Number, Number>[] lc1 = new LineChart[2];

        for (int i = 0; i < 2; i++) {
            acceGraph[i] = new AcceGraph(i);
            baroGraph[i] = new BaroGraph(i);

            lc[i] = acceGraph[i].getLineChart();
            lc1[i] = baroGraph[i].getLineChart();

            acceGraph[i].executeGraph();
            baroGraph[i].executeGraph();
        }

        GridPane gridPane = new GridPane();
        gridPane.add(stopButton,0,0);
        gridPane.add(modeButton1, 0, 1);
        gridPane.add(modeButton2, 0, 2);

        FlowPane root = new FlowPane();
        root.getChildren().addAll(lc[0], lc1[0], gridPane, lc[1], lc1[1]);

        Scene scene = new Scene(root, 1600, 900);

        primaryStage.setTitle("SSNS Project");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        FunctionsWrapper.startEverything();
        launch(args);
    }
}
