package com.example.projet_informatique_servicemusique;

import com.opencsv.CSVWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AdminController {

    @FXML
    private TextField txt_Titre;
    @FXML
    private TextField txt_Artiste;
    @FXML
    private TextField txt_Album;
    @FXML
    private TextField txt_Fichier;

    @FXML
    protected void btn_Ajouter_Click() {
        AjouterMotCSV(txt_Titre.getText(), txt_Artiste.getText(), txt_Album.getText(), txt_Fichier.getText());
    }

    public void AjouterMotCSV(String Titre, String NomArtiste, String NomAlbum, String NomMusique)
    {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(new File("src/main/resources/BaseDonnee.csv").getAbsolutePath(), true),';')) {
            String[] Test1 = {Titre, NomArtiste, NomAlbum, NomMusique};
            csvWriter.writeNext(Test1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void btn_Exit_Clicked() throws IOException {
        // Lorque l'on sort de la vue admin, on
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene newScene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) txt_Titre.getScene().getWindow();
        stage.setTitle("TDT - Login");
        stage.setScene(newScene);
        stage.setResizable(false);
        stage.show();
    }
}