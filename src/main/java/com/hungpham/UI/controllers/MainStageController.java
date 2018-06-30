package com.hungpham.UI.controllers;

import com.hungpham.Controller.SerialPortController;
import com.hungpham.GraphStage;
import com.hungpham.UI.MainScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import static com.hungpham.Main.firstTimeOpen;
import static com.hungpham.Main.mainStage;
import static com.hungpham.StaticControlVariables.serialCommands;
import static com.hungpham.UI.MainScene.*;

public class MainStageController {
    @FXML
    private ListView<Item> itemListView;

    @FXML
    private ListView<Item> itemListView1;

    @FXML
    private Button nextButton;

    @FXML
    private Button connectButton;

    @FXML
    private Button disconnectButton;

    @FXML
    private Button scanButton;

    @FXML
    private MenuItem newMenuItem;

    @FXML
    private MenuItem quitMenuItem;

    @FXML
    private MenuItem removeMenuItem;

    private ObservableList<Item> hostList;
    private ObservableList<Item> sensortagList;

    private MainScene mainScene;

    private SerialPortController[] serialPortController;

    public void initialize() {
        hostList = FXCollections.observableArrayList();
        sensortagList = FXCollections.observableArrayList();

        itemListView.setItems(hostList);
        itemListView1.setItems(sensortagList);

        mainScene = new MainScene();

        registerEventHandlers();

        if (firstTimeOpen) {
            try {
                mainScene.checkPort();
                if (portsList.size() > 0) {
                    operatingDevicesNumber = portsList.size();
                    serialCommands = new LinkedBlockingQueue[portsList.size()];
                    for (int i = 0; i < portsList.size(); i++) {
                        serialCommands[i] = new LinkedBlockingQueue<>();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("No Launchpad connecting to this computer!");
                    alert.setContentText("Please connect Launchpad(s) before opening this application!");

                    alert.showAndWait();
                    Platform.exit();
                    System.exit(0);
                }
                serialPortController = new SerialPortController[portsList.size()];
                for (int i = 0; i < portsList.size(); i++) {
                    hostList.add(new Item("Launchpad at " + portsList.get(i)));
                    serialPortController[i] = new SerialPortController(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Platform.exit();
                System.exit(0);
            }
        } else {
            GraphStage.command = "";
            for (int i = 0; i < portsList.size(); i++) {
                hostList.add(new Item("Launchpad at " + portsList.get(i)));
                disconnectSensors();
            }
        }
    }


    private void registerEventHandlers() {
        //Buttons
        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    nextStage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                connectSensors();
            }
        });

        disconnectButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                disconnectSensors();
            }
        });

        scanButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                generateHostandSensors();
            }
        });

        //


        quitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.SHORTCUT_DOWN));
        quitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

        newMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
        newMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                hostList.clear();
                sensortagList.clear();
            }
        });

        removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Item item = itemListView.getSelectionModel().getSelectedItem();
                hostList.remove(item);

            }
        });
        nextButton.setDisable(true);
        disconnectButton.setDisable(true);
        connectButton.setDisable(true);
    }

    private void generateHostandSensors() {
        try {
            serialCommands[0].add("01030C00");
            Thread.sleep(200);
            serialCommands[0].add("0100FE260805000000000000000000000000000000000000000000000000000000000000000001000000");
            Thread.sleep(200);
            serialCommands[0].add("0131FE0115");
            Thread.sleep(200);
            serialCommands[0].add("0131FE0116");
            Thread.sleep(200);
            serialCommands[0].add("0131FE011A");
            Thread.sleep(200);
            serialCommands[0].add("0131FE0119");
            Thread.sleep(200);
            serialCommands[0].add("0104FE03030100");
            Thread.sleep(30000);
            for (String stk : stkMacSet) {
                sensortagList.add(new Item("SensorTag " + stk));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        connectButton.setDisable(false);
    }

    private void connectSensors() {
        sensortagList.clear();
        Iterator macIterator = stkMacSet.iterator();
        try {
            for (int i = 0; i < portsList.size(); i++) {
                serialCommands[i].add("01030C00");
                Thread.sleep(200);
                serialCommands[i].add("0100FE260805000000000000000000000000000000000000000000000000000000000000000001000000");
                Thread.sleep(200);
                serialCommands[i].add("0131FE0115");
                Thread.sleep(200);
                serialCommands[i].add("0131FE0116");
                Thread.sleep(200);
                serialCommands[i].add("0131FE011A");
                Thread.sleep(200);
                serialCommands[i].add("0131FE0119");
                Thread.sleep(200);
                String mac = (String) macIterator.next();
                serialCommands[i].add("0109FE09000000" + mac);
                Thread.sleep(200);
                serialCommands[i].add("0192FD0600002D000100");
                Thread.sleep(200);
                serialCommands[i].add("0192FD06000025000100");
                Thread.sleep(200);
                serialCommands[i].add("0192FD050000310010");
                Thread.sleep(200);
                serialCommands[i].add("0192FD050000290010");
                Thread.sleep(200);
                serialCommands[i].add("0192FD0600002F003802");
                Thread.sleep(200);
                serialCommands[i].add("018AFD0400002F00");
                Thread.sleep(200);
                serialCommands[i].add("0192FD050000270001");
                Thread.sleep(200);
                sensortagList.add(new Item("Connected: " + mac));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        connectButton.setDisable(true);
        disconnectButton.setDisable(false);
        nextButton.setDisable(false);
    }

    private void disconnectSensors() {
        sensortagList.clear();
        for (int i = 0; i < portsList.size(); i++) {
            serialCommands[i].add("010AFE03000013");
            serialCommands[i].add("010AFE03010013");
        }
        for (String stk : stkMacSet) {
            sensortagList.add(new Item("SensorTag " + stk));
        }
        connectButton.setDisable(false);
        scanButton.setDisable(false);
        disconnectButton.setDisable(true);
        nextButton.setDisable(true);
    }

    private void nextStage() throws Exception {
        try {
//            URL url = new File("resources/fxmls/FallDetectedStage.fxml").toURL();
//            Parent root = FXMLLoader.load(url);
//            Stage stage = (Stage) scanButton.getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.show();

            SerialPortController.mode = 1;
            Application graph = new GraphStage();
            graph.start(mainStage);

            //Stage stage = new Stage();
            //stage.initModality(Modality.APPLICATION_MODAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
