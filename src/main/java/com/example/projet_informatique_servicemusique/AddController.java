package com.example.projet_informatique_servicemusique;

import com.opencsv.CSVWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class AddController implements Initializable {

    // Attributs
    // Champs de texte
    @FXML
    TextField txt_Titre;
    @FXML
    TextField txt_Artiste;
    @FXML
    TextField txt_Album;

    // Pour récupérer les fichiers
    @FXML
    ImageView img_ImageFile;
    @FXML
    Label lbl_SongFile;

    // Titre de la vue
    @FXML
    Label lbl_TitrePage;

    // Bouton enregister et continuer
    @FXML
    Button btn_SaveContinue;

    // Les attributs du programme
    File imageFile = null;
    File songFile = null;
    String imageName;
    String songName;
    Boolean modifying = false;
    Boolean imageModified = false;
    Boolean songModified = false;

    // Le hello controller
    private HelloController helloController;

    // Stock les anciennes valeurs en cas de modification
    String oldTitre;
    String oldArtiste;
    String oldAlbum;
    String oldImageName;
    String oldSongName;
    HBox oldHBox;

    // Les constantes
    final String defaultImagePath = "src/main/resources/Pictures/ImageExemple.png";
    final String imageFolder = "src/main/resources/Pictures/";
    final String songFolder = "src/main/resources/Songs/";
    final String csvDonneePath = "src/main/resources/BaseDonnee.csv";
    final String incompatibleImagePath = "src/main/resources/Pictures/incompatibleImage.png";

    // Liste des extensions de fichier accepté
    final String[] listSongExtension = {"mp3", "wav", "aiff", "au", "flac", "ogg", "aac", "m4a", "wma"};
    final String[] listImageExtension = {"png", "jpg", "jpeg", "bmp", "tiff", "tif", "ico", "svg"};


    /**
     * Lorsque on a terminé d'ajouter une chanson mais on veut en mettre d'autres
     * @throws IOException
     */
    @FXML
    protected void btn_SaveContinue_Click() throws IOException {
        // Vérifier si l'entrée est complète
        if (txt_Titre.getText().equals("") || txt_Artiste.getText().equals("") || txt_Album.getText().equals("") || imageFile == null || songFile == null) {
            showConfirmation(false);
        }
        else {
            saveSequence(false);
        }
    }

    /**
     * Lorsque que l'on a terminé l'ajout de musique
     * @throws IOException
     */
    @FXML
    protected void btn_SaveQuit_Click() throws IOException {
        // Vérifier si l'entrée est complète
        if (txt_Titre.getText().equals("") || txt_Artiste.getText().equals("") || txt_Album.getText().equals("") || imageFile == null || songFile == null) {
            showConfirmation(true);
        }
        else {
            saveSequence(true);
        }
    }

    /**
     * Permet d'afficher la vue de confirmation
     * @param quitWindow    : Valeur Bool incdiquant si il faut quitter la fenêtre
     * @throws IOException
     */
    void showConfirmation(Boolean quitWindow) throws IOException {
        Stage primaryStage = (Stage) txt_Titre.getScene().getWindow();

        // Créer une nouvelle fenêtre pop-up
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("confirm-view.fxml"));
        Scene newScene = new Scene(fxmlLoader.load());

        // Passer cette instance à la vue
        ConfirmController confirmController = fxmlLoader.getController();
        confirmController.setParentController(this);
        confirmController.setQuitWindow(quitWindow);

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(primaryStage);
        popupStage.initStyle(StageStyle.UTILITY);
        popupStage.setTitle("TDT - confirm");
        popupStage.setScene(newScene);

        popupStage.setResizable(false);
        popupStage.setWidth(580);
        popupStage.setHeight(350);

        popupStage.show();
    }

    // Méthode utilisé pour lancer l'enregistement

    /**
     * Séquence de sauvegarde de la nouvelle musique
     * @param quitWindow
     */
    void saveSequence(Boolean quitWindow) {

        // Si l'entrée est marquée comme entrain d'être modifier, il faut supprimer l'ancienne entrée
        if (modifying) {
            // Suppression de l'ancienne valeur
            helloController.SupprimerLigne(oldTitre, oldArtiste, oldAlbum, oldImageName, oldSongName, oldHBox, imageModified, songModified);
        }

        // Enregister la nouvelle entrée
        // Sauvegarde des fichiers (image, son)
        if (imageModified) {
            imageName = saveFile(imageFile, imageFolder);
        }

        if (songModified) {
            songName = saveFile(songFile, songFolder);
        }

        // Enregistement de la musique dans le fichier csv
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(new File(csvDonneePath).getAbsolutePath(), true),';')) {
            String[] Test1 = {txt_Titre.getText(), txt_Artiste.getText(), txt_Album.getText(), imageName, songName};
            csvWriter.writeNext(Test1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Mettre la liste de musique à jour
        helloController.refreshList(txt_Titre.getText(), txt_Artiste.getText(), txt_Album.getText(), imageName, songName);

        // Quitter la fenêtre
        if (quitWindow) {
            Stage stage = (Stage) lbl_SongFile.getScene().getWindow();
            stage.close();
        }
        else {
            // Mettre la page à zéro
            // Clean textField
            txt_Titre.setText("");
            txt_Artiste.setText("");
            txt_Album.setText("");

            // Clean Image
            loadImage(new File(defaultImagePath));

            // Clean Song drop zone
            lbl_SongFile.setStyle("-fx-background-color: transparent; -fx-border-width: 10");
        }
    }

    /** Méthode s'exécutant lors de l'initialisation de la vue
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Gestion du glisser-déposer pour l'image
        img_ImageFile.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
            }
            event.consume();
        });

        img_ImageFile.setOnDragDropped(event -> {
            var db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                File imageFileTemp = db.getFiles().get(0);
                if (verifyExtension(imageFileTemp, listImageExtension)) {
                    imageFile = imageFileTemp;
                    loadImage(imageFile);
                    imageModified = true;
                }
                else {
                    loadImage(new File(incompatibleImagePath));
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        // Gestion du glisser-déposer pour la musique
        lbl_SongFile.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
            }
            event.consume();
        });

        lbl_SongFile.setOnDragDropped(event -> {
            var db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                File songFileTemp = db.getFiles().get(0);
                if (verifyExtension(songFileTemp, listSongExtension)) {
                    songFile = songFileTemp;
                    lbl_SongFile.setStyle("-fx-background-color: #0CCE6B; -fx-border-width: 0; -fx-background-radius: 17");
                    lbl_SongFile.setText(songFile.getName());
                    songModified = true;
                }
                else {
                    lbl_SongFile.setStyle("-fx-background-color: #340B0B; -fx-border-width: 0; -fx-background-radius: 17");
                    lbl_SongFile.setText("incompatible file");
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        // Gestion de la récupération d'image à partir du gestionnaire de fichier
        img_ImageFile.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            File imageFileTemp = fileChooser.showOpenDialog(img_ImageFile.getScene().getWindow());
            if (imageFileTemp.exists()) {
                if (verifyExtension(imageFileTemp, listImageExtension)) {
                    imageFile = imageFileTemp;
                    loadImage(imageFile);
                    imageModified = true;
                }
                else {
                    loadImage(new File(incompatibleImagePath));
                }
            }
        });

        // Gestion de la récupération du fichier musique à partir du gestionnaire de fichier
        lbl_SongFile.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            File songFileTemp = fileChooser.showOpenDialog(lbl_SongFile.getScene().getWindow());
            if (songFileTemp.exists()) {
                if (verifyExtension(songFileTemp, listSongExtension)) {
                    songFile = songFileTemp;
                    lbl_SongFile.setStyle("-fx-background-color: #0CCE6B; -fx-border-width: 0; -fx-background-radius: 17");
                    lbl_SongFile.setText(songFile.getName());
                    songModified = true;
                }
                else {
                    lbl_SongFile.setStyle("-fx-background-color: #340B0B; -fx-border-width: 0; -fx-background-radius: 17");
                    lbl_SongFile.setText("incompatible file");
                }
            }
        });

        // Permet de détecter le TAB
        txt_Titre.setOnKeyPressed(this::handleKeyPress_txt_Titre);
        txt_Artiste.setOnKeyPressed(this::handleKeyPress_txt_Artiste);
        txt_Album.setOnKeyPressed(this::handleKeyPress_txt_Album);
    }

    /**
     * Initialise pour préparer à la modification
     */
    void initialiseFileToModify() {
        // Enregister l'image
        imageFile = new File(imageFolder + imageName);
        loadImage(imageFile);

        // Enregister la musique
        songFile = new File(songFolder + songName);
    }

    /**
     * Gere le bouton "TAB"
     * @param event
     */
    private void handleKeyPress_txt_Titre(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            // Définir le focus sur le champs de texte "password"
            txt_Artiste.requestFocus();
        }
    }

    /**
     * Gére le bouton "TAB"
     * @param event
     */
    private void handleKeyPress_txt_Artiste(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            // Définir le focus sur le champs de texte "username";
            txt_Album.requestFocus();
        }
    }

    /**
     * Gére le bouton "TAB"
     * @param event
     */
    private void handleKeyPress_txt_Album(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            // Définir le focus sur le champs de texte "username";
            txt_Titre.requestFocus();
        }
    }

    /**
     * Enregister le fichier dans le dossier
     * @param file
     * @param dossier
     * @return
     */
    private String saveFile(File file, String dossier) {
        try {
            File saveLocation = new File(dossier + file.getName());
            int count = 0;
            while (saveLocation.exists()) {
                count++;
                String fileName = file.getName();
                String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
                String extension = fileName.substring(fileName.lastIndexOf('.'));
                String newFileName = baseName + "_" + count + extension;
                saveLocation = new File(dossier + newFileName);
            }
            Files.copy(file.toPath(), saveLocation.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
            return saveLocation.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mettre à jour l'image lorsque on l'a modifié
     * @param file
     */
    private void loadImage(File file) {
        Image image = new Image(file.toURI().toString());
        img_ImageFile.setImage(image);
    }

    /**
     * Utiliser pour définir le helloController
     * @param helloController
     */
    void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    /**
     * Méthode appelé lorsque l'on souhaite modifier des valeurs
     * @param oldTitre
     * @param oldArtiste
     * @param oldAlbum
     * @param oldImageName
     * @param oldSongName
     * @param oldHBox
     */
    void setModify(String oldTitre, String oldArtiste, String oldAlbum, String oldImageName, String oldSongName, HBox oldHBox) {
        // On marque l'action comme modification
        modifying = true;

        // On stock l'ancienne HBox
        this.oldTitre = oldTitre;
        this.oldArtiste = oldArtiste;
        this.oldAlbum = oldAlbum;
        this.oldImageName = oldImageName;
        this.oldSongName = oldSongName;
        this.oldHBox = oldHBox;

        // Initialisation du titre de la vue
        lbl_TitrePage.setText("Modifier un titre");

        // Rendre le bouton "enregister et continuer" invisible
        btn_SaveContinue.setVisible(false);
        btn_SaveContinue.setMaxWidth(0);

        // Initialisation des textes
        txt_Titre.setText(oldTitre);
        txt_Artiste.setText(oldArtiste);
        txt_Album.setText(oldAlbum);

        // Enregistement du nom du fichier de l'image
        imageName = oldImageName;

        // Enregistrement du nom du fichier de la musique
        songName = oldSongName;

        // Rendre le label du son dans son mise en forme de modification
        lbl_SongFile.setText(songName);
    }

    /**
     * Vérifie l'extention du fichier. Retourne "true" si l'extension du fichier transmit est dans la liste des extentions fournis
     * @param file                      Fichier à vérifier
     * @param compatibleExtensionList   Liste des extensions accepté
     * @return                          Retourne TRUE si l'extension du fichier est dans la liste, sinon retourne FALSE
     */
    Boolean verifyExtension(File file, String[] compatibleExtensionList) {
        if (file.exists()) {
            String fileName = file.getName();
            String fileExtension = "";

            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
                fileExtension = fileName.substring(dotIndex + 1);
            }

            for (String s : compatibleExtensionList) {
                if (fileExtension.toLowerCase().equals(s)) {
                    return true;
                }
            }
        }
        return false;
    }
}