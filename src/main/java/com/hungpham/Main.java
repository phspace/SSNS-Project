package com.hungpham;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    public static Stage mainStage;

    public static volatile boolean firstTimeOpen = true;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        URL url = new File("resources/fxmls/MainStage.fxml").toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Fall Detection");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
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
        launch(args);
    }
}