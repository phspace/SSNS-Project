package com.hungpham.UI;

import com.hungpham.GraphStage;
import javafx.application.Application;
import javafx.stage.Stage;
import jssc.SerialPortList;

import java.util.ArrayList;
import java.util.HashSet;

public class MainScene extends Application {
    public static volatile ArrayList<String> portsList = new ArrayList<>();

    public static int operatingDevicesNumber = 0;
    public static volatile HashSet<String> stkMacSet = new HashSet<>();



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

    }

    public void checkPort() {
        String[] portNames = SerialPortList.getPortNames();

        if (portNames.length == 0) {
            System.out.println("There are no serial-ports.");
        }

        for (int i = 0; i < portNames.length; i++) {
            int portLastNumber = Integer.parseInt(portNames[i].substring(portNames[i].length() - 1));
            if (portLastNumber % 2 > 0) {
                portsList.add(portNames[i]);
                System.out.println(portNames[i]);
            }
        }
    }

    public void checkReadyOperating() {
        operatingDevicesNumber = GraphStage.mode;
    }

}
