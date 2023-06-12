package com.example.projet_informatique_servicemusique;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Défini le controller de la vue "confirm-view"
 */
public class ConfirmController implements Initializable {

    // Déclaration des labels
    @FXML
    Label lbl_Titre;
    @FXML
    Label lbl_Prompt;

    // Déclaration des boutons
    @FXML
    Button btn_Default;

    // Déclaration du controleur mère
    AddController addController;
    Boolean quitWindow;

    /**
     * Lorsque l'on appuie sur le bouton "annuler", on ferme la fenetre de confirmation
     */
    @FXML
    protected void btn_Default_Click() {
        // On quite la page
        quitWindows();
    }

    /**
     * Fonction permettant de quitter la fenêtre
     */
    void quitWindows() {
        Stage stage = (Stage) lbl_Titre.getScene().getWindow();
        stage.close();
    }

    /**
     * Méthode s'executant lors de l'initialisation
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Mise en forme des textes
        lbl_Titre.setText("Avertissement");
        lbl_Prompt.setText("Certain champs ne sont pas remplis correctement");
        btn_Default.setText("Annuler");
    }

    /**
     * Permet de recevoir la référence du controleur mère
     * @param addController     Controller de la vue mère
     */
    void setParentController(AddController addController) {
        this.addController = addController;
    }

    /**
     * Setter de la variable "quitWindow"
     * @param quitWindow
     */
    void setQuitWindow (Boolean quitWindow) {
        this.quitWindow = quitWindow;
    }
}
