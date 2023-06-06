package com.example.projet_informatique_servicemusique;

import com.opencsv.CSVReader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class LoginController {

    @FXML
    protected TextField txt_UserName;
    @FXML
    protected PasswordField txt_Password;
    @FXML
    protected Button btn_Confirm;


    public void initialize() {
        txt_UserName.setOnKeyPressed(this::handleKeyPress_Username);
        txt_Password.setOnKeyPressed(this::handleKeyPress_Password);
    }

    private void handleKeyPress_Username(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            // Définir le focus sur le champs de texte "password"
            txt_Password.requestFocus();
        }
    }

    private void handleKeyPress_Password(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            // Définir le focus sur le champs de texte "username";
            txt_UserName.requestFocus();
        }
    }

    @FXML
    protected void btn_Confirm_Click() throws IOException {
        verificationUser();
    }
    @FXML
    protected void txt_UserName_OnAction() throws IOException {
        verificationUser();
    }
    @FXML
    protected void txt_Password_OnAction() throws IOException {
        verificationUser();
    }

    @FXML
    protected void svg_Logo_OnClick() throws IOException {
        if (Objects.equals(txt_UserName.getText(), "admin") && Objects.equals(txt_Password.getText(), "admin")) {
            // On marque l'utilisateur comme admin
            HelloController.isAdmin = true;

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btn_Confirm.getScene().getWindow();
            stage.setTitle("TDT");
            stage.setResizable(true);
            stage.setScene(newScene);

            //Définir une taille minimale pour la fenêtre
            stage.setMinWidth(1000);
            stage.setMinHeight(500);
            stage.setHeight(1200);
            stage.setHeight(800);

            stage.show();

        }
    }

    public boolean searchAndValidateUser(String userName, String hashedPassword) {
        try (CSVReader csvReader = new CSVReader(new FileReader(new File("src/main/resources/UserList.csv").getAbsolutePath()), ';')) {
            String[] ligne;
            while ((ligne = csvReader.readNext()) != null) {
                if (Objects.equals(ligne[0], userName) && Objects.equals(ligne[1], hashedPassword)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void verificationUser() throws IOException {
        // Contrôle de l'autorisation
        boolean isAutorised = false;
        isAutorised = searchAndValidateUser(txt_UserName.getText(), txt_Password.getText());

        if (isAutorised) {
            // On marque l'utilisateur commme pas admin
            HelloController.isAdmin = false;

            // Si autorisé, on affiche la page "hello-view"
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btn_Confirm.getScene().getWindow();
            stage.setTitle("TDT");
            stage.setResizable(true);
            stage.setScene(newScene);

            //Définir une taille minimale pour la fenêtre
            stage.setMinWidth(1000);
            stage.setMinHeight(500);
            stage.setHeight(1200);
            stage.setHeight(800);

            stage.show();
        }
        else {
            // Si pas autorisé on affiche le texte des champs de textes en rouge
            txt_UserName.setStyle("-fx-text-fill: red");
            txt_Password.setStyle("-fx-text-fill: red");
            txt_Password.setText("");
            txt_Password.requestFocus();
        }
    }
}