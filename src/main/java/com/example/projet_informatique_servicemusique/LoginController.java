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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Controller de la vue "login-view"
 */
public class LoginController {

    @FXML
    protected TextField txt_UserName;       // Champ de texte du nom d'utilisateur
    @FXML
    protected PasswordField txt_Password;   // Champ de texte du mot de passe
    @FXML
    protected Button btn_Confirm;           // Bouton pour valider les entrées

    final String adminUsername = "admin";   // Nom d'utilisateur de l'admin
    final String adminPassword = "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918";    // Mot de passe de l'admin. Celui ci est hacher en SHA 256
    final String userListPath = "src/main/resources/UserList.csv";                                      // Chemin de fichier csv de la liste des utilisateurs

    /**
     * Permet de surveiller lorsque la touche TAB est pressé
     */
    public void initialize() {
        txt_UserName.setOnKeyPressed(this::handleKeyPress_Username);
        txt_Password.setOnKeyPressed(this::handleKeyPress_Password);
    }

    /**
     * Detecte le bouton TAB sur champ de texte du nom d'utilisateur
     * @param event
     */
    private void handleKeyPress_Username(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            // Définir le focus sur le champs de texte "password"
            txt_Password.requestFocus();
        }
    }

    /**
     * Detecte le bouton TAB sur champ de texte du mot de passe
     * @param event
     */
    private void handleKeyPress_Password(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            // Définir le focus sur le champs de texte "username";
            txt_UserName.requestFocus();
        }
    }

    /**
     * When button "Confirm" pressed --> Call "verificationUser"
     * @throws IOException
     */
    @FXML
    protected void btn_Confirm_Click() throws IOException {
        verificationUser();
    }
    /**
     * When ENTER on TextField "Username" pressed --> Call "verificationUser"
     * @throws IOException
     */
    @FXML
    protected void txt_UserName_OnAction() throws IOException {
        verificationUser();
    }
    /**
     * When ENTER on TextField "Password" pressed --> Call "verificationUser"
     * @throws IOException
     */
    @FXML
    protected void txt_Password_OnAction() throws IOException {
        verificationUser();
    }

    /**
     * Lorsque l'on appuie sur le logo, on verifie si "Username et Password" si cela correspond à l'admin, on ouvre la
     * page principal en mode "admin"
     * @throws IOException
     */
    @FXML
    protected void svg_Logo_OnClick() throws IOException {
        if (Objects.equals(txt_UserName.getText(), adminUsername) && Objects.equals(hashPassword(txt_Password.getText()), adminPassword)) {
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
            stage.setWidth(1400);
            stage.setHeight(800);

            stage.show();
        }
    }

    /**
     * Fonction permettant de verifier si l'utilisateur est présent dans la liste de utilisateur.
     * @param userName
     * @param hashedPassword
     * @return Retourne une valeur Bool. TRUE = utilisateur trouvé et mot de passe correct. Sinon --> FALSE
     */
    public boolean searchAndValidateUser(String userName, String hashedPassword) {
        try (CSVReader csvReader = new CSVReader(new FileReader(new File(userListPath).getAbsolutePath()), ';')) {
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

    /**
     * Si l'utilisateur figure sur la "userList" et que le mot de passe est correct, on affiche la page principal
     * @throws IOException
     */
    public void verificationUser() throws IOException {
        // Contrôle de l'autorisation
        boolean isAutorised = false;
        isAutorised = searchAndValidateUser(txt_UserName.getText(), hashPassword(txt_Password.getText()));

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
            stage.setWidth(1400);
            stage.setHeight(800);

            stage.show();
        }
        else {
            // Si pas autorisé on affiche le texte des champs de textes en rouge
            txt_UserName.setStyle("-fx-text-fill: red; -fx-background-color: #ffffff; -fx-background-radius: 15");
            txt_Password.setStyle("-fx-text-fill: red; -fx-background-color: #ffffff; -fx-background-radius: 15");
            txt_Password.setText("");
            txt_Password.requestFocus();
        }
    }

    /** Méthode pour hacher le mot de passe
     * @param password Mot de passe à hacher
     * @return Retourne le mot de passe haché
     */
    public static String hashPassword(String password) {
        try {
            // Créer un objet MessageDigest avec l'algorithme SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convertir le mot de passe en tableau de bytes
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

            // Calculer le hachage du mot de passe
            byte[] hashedBytes = digest.digest(passwordBytes);

            // Convertir le tableau de bytes en représentation hexadécimale
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}