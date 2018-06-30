package com.hungpham.UI.controllers;

import com.hungpham.UI.MainScene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class FallStageController {


    @FXML private Button backButton;

    public void initialize(){
        registerEventHandlers();
    }

    private void registerEventHandlers() {
        //Buttons
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    nextStage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void nextStage(){
        try{
            URL url = new File("resources/fxmls/MainStage.fxml").toURL();
            Parent root = FXMLLoader.load(url);
            Stage stage = (Stage)backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setMinHeight(600);
            stage.setMinWidth(800);
            stage.show();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
