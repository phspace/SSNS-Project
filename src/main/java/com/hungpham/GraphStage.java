package com.hungpham;

import com.hungpham.Controller.SerialPortController;
import com.hungpham.UI.graphs.AcceGraph;
import com.hungpham.UI.graphs.BaroGraph;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.hungpham.Main.firstTimeOpen;

public class GraphStage extends Application{
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

        RunBackground runBackground = new RunBackground();
        runBackground.start();

        Button stopButton = new Button("Back to Main Page");
        stopButton.setMinSize(200, 50);

        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                command = "stop";
                firstTimeOpen = false;
                SerialPortController.mode = 0;
                URL url = null;
                Parent root = null;
                try {
                    url = new File("resources/fxmls/MainStage.fxml").toURL();
                    root = FXMLLoader.load(url);
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                Stage stage = (Stage)stopButton.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));
                stage.setMinHeight(600);
                stage.setMinWidth(800);
                stage.show();
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

    static class RunBackground extends Thread {
        @Override
        public void run() {
            FunctionsWrapper.startEverything();
        }
    }
}
