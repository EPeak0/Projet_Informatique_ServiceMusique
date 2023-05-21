package com.example.projet_informatique_servicemusique;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("TDT");
        stage.setScene(scene);

        // Définir une taille minimale pour la fenêtre
        stage.setMinWidth(800);
        stage.setMinHeight(500);

        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}