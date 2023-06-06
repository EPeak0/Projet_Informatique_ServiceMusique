package com.example.projet_informatique_servicemusique;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("TDT - Login");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setMinWidth(0);
        stage.setMinHeight(0);
        stage.setWidth(400);
        stage.setHeight(450);

        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}