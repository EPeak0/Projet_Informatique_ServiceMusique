package com.example.projet_informatique_servicemusique;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Défini une musique
 */
public class Music {
    // Définition des attributs

    // Défini la musique
    String titre;
    String auteur;
    String album;
    String imagePath;
    String songPath;
    Image imageFile;

    // Contient le HelloController
    HelloController helloController;

    // Contient la hbox représentant la ligne sur la vue
    HBox groupe = new HBox();
    Label lbl_Titre;
    Button btn_Delete;
    Button btn_Confirm;
    SVGPath svgModify;
    SVGPath svgTrash;

    // Constantes
    final String imagesFolder = "src/main/resources/Pictures/";

    // Chemins SVG
    final String svgPathModify = "M 0.4019 -6.6962 L 2.2058 -21.8205 Q 2.3397 -24.0526 3.3397 -25.7846 L 24.8397 -63.0237 Q 26.8397 -66.4878 30.3038 -64.4878 L 40.6962 -58.4878 Q 44.1603 -56.4878 42.1603 -53.0237 L 20.6603 -15.7846 Q 19.6603 -14.0526 17.7942 -12.8205 L 5.5981 -3.6962 Q 0 0 0.4019 -6.6962";
    final String svgPathTrash = "M -2 -3 L -3 -2 L -1 0 L -3 2 L -2 3 L 0 1 L 2 3 L 3 2 L 1 0 L 3 -2 L 2 -3 L 0 -1 L -2 -3";

    // Constructeur
    Music(String titre, String auteur, String album, String imagePath, String songPath, HelloController helloController) {
        this.titre = titre;
        this.auteur = auteur;
        this.album = album;
        this.imagePath = imagePath;
        this.songPath = songPath;
        this.helloController = helloController;
    }


    /** Méthode utilisé pour créer la ligne sur la visu
     * @return  Retourne un hbox contenant la ligne correspondant à la musique
     */
    HBox creerGroupe() {
        // Declaration des objets
        lbl_Titre = new Label(titre);
        Label lbl_artiste = new Label(auteur);
        Label lbl_album = new Label(album);
        ImageView image = new ImageView();
        btn_Delete = new Button();
        btn_Confirm = new Button();

        // Definition de la police d'écriture
        Font fontBig = Font.font("Arial Rounded MT Bold", 18);
        Font font = Font.font("Arial Rounded MT Bold");

        // Mise en forme des labels
        lbl_Titre.setStyle("-fx-text-fill: #FFFFFF");
        lbl_Titre.setFont(fontBig);
        lbl_Titre.setPadding(new Insets(0,0,0,20));
        lbl_Titre.setAlignment(Pos.CENTER_LEFT);

        lbl_artiste.setStyle("-fx-text-fill: #787878");
        lbl_artiste.setFont(font);
        lbl_artiste.setPadding(new Insets(0,0,0,20));
        lbl_artiste.setAlignment(Pos.CENTER_LEFT);

        lbl_album.setStyle("-fx-text-fill: #FFFFFF");
        lbl_album.setFont(font);
        lbl_album.setPadding(new Insets(0,0,0,0));
        lbl_album.setAlignment(Pos.CENTER_LEFT);
        lbl_album.setPrefWidth(350);

        // Mettre en forme les boutons de l'admin (supprimer et confirmer)
        if (HelloController.isAdmin) {
            // Bouton modifier
            svgModify = new SVGPath();
            svgModify.setContent(this.svgPathModify); // Exemple de chemin SVG
            svgModify.setFill(Color.web("#FFFFFF"));
            svgModify.setScaleX(0.3);
            svgModify.setScaleY(0.3);
            btn_Confirm.setGraphic(svgModify);
            btn_Confirm.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF");

            // Bouton supprimer
            svgTrash = new SVGPath();
            svgTrash.setContent(this.svgPathTrash); // Exemple de chemin SVG
            svgTrash.setFill(Color.web("#FFFFFF"));
            svgTrash.setScaleX(2);
            svgTrash.setScaleY(2);
            btn_Delete.setGraphic(svgTrash);
            btn_Delete.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFFFF;");
        }
        else {

            // Autrement, on désactive les boutons "confirm" et "delete"
            btn_Delete.setDisable(true);
            btn_Confirm.setDisable(true);
            btn_Delete.setOpacity(0);
            btn_Confirm.setOpacity(0);
        }

        // Mettre en forme l'image de la pochette
        // On récupère les liens de l'image de pochette et de l'image du bouton play
        String path = imagesFolder + imagePath;
        File imageFile = new File(path);
        String localUrl = null;
        try {
            localUrl = imageFile.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // Mettre l'ImageView comme il faut
        this.imageFile = new Image(localUrl);
        image.setImage(this.imageFile);
        image.setFitHeight(50);
        image.setFitWidth(50);
        image.setStyle("-fx-background-radius: 10px;");
        HBox.setHgrow(image, Priority.NEVER);

        // Préparation de la VBox contenant le titre et l'artiste (auteur)
        VBox titreArtiste = new VBox();
        titreArtiste.setPadding(new Insets(0,0,0,0));
        titreArtiste.getChildren().addAll(lbl_Titre, lbl_artiste);
        titreArtiste.setPrefWidth(350);
        titreArtiste.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(titreArtiste, Priority.ALWAYS);

        // Préparation des séparations
        Separator separator = new Separator();
        HBox.setHgrow(separator, Priority.ALWAYS);
        separator.setFocusTraversable(false);
        separator.setVisible(false);

        // Preparation de la HBox "groupe" qui ira dans une ligne de la listView
        groupe.setPadding(new Insets(0,20,0,10));
        groupe.setStyle("-fx-background-radius: 10;");
        groupe.getChildren().addAll(image, titreArtiste, lbl_album, btn_Confirm, btn_Delete);
        groupe.setAlignment(Pos.CENTER);

        // Faire en sorte que l'image s'agrandisse lorsque l'on passe le curseur par dessus la ligne de la listView
        groupe.setOnMouseEntered(event -> {
            image.setScaleX(1.1);
            image.setScaleY(1.1);
        });

        // On réduit l'image de la pochette quitte la ligne de la listView
        groupe.setOnMouseExited(event -> {
            image.setScaleX(1);
            image.setScaleY(1);
            svgTrash.setFill(Color.web("#FFFFFF"));
        });

        // Suppression de la musique de la ligne
        btn_Delete.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if (svgTrash.getFill().equals(Color.RED)) {
                    // Code à exécuter lorsque le bouton "supprimer" est cliqué
                    helloController.SupprimerLigne(lbl_Titre.getText(), lbl_artiste.getText(), lbl_album.getText(), imagePath, songPath, groupe, true, true);
                }
                else {
                    svgTrash.setFill(Color.RED);
                }
            }
        });

        // Confirmation d'une ligne
        btn_Confirm.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                // Appel de la fenêtre permettant de modifer les informations du titre
                Stage primaryStage = (Stage) helloController.lbl_Titre.getScene().getWindow();

                // Créer une nouvelle fenêtre pop-up

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-view.fxml"));
                Scene newScene = null;
                try {
                    newScene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Passer cette instance à la vue
                AddController addController = fxmlLoader.getController();
                addController.setHelloController(helloController);
                addController.setModify(titre, auteur, album, imagePath, songPath, groupe);

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.initOwner(primaryStage);
                popupStage.initStyle(StageStyle.UTILITY);
                popupStage.setTitle("TDT - modify a song");
                popupStage.setScene(newScene);

                popupStage.setResizable(false);
                popupStage.setWidth(800);
                popupStage.setHeight(450);

                popupStage.show();
                addController.initialiseFileToModify();
            }
        });

        // Detection du "click" --> Lancer la musique
        groupe.setOnMouseClicked(event -> {
            System.out.println("Élément de la liste cliqué !");
            System.out.println("Titre: " + titre);
            System.out.println("Artiste: " + auteur);
            System.out.println("Album: " + album);

            LectureMusique();
        });

        return groupe;
    }

    /**
     * Lance la lecture de la musique
     */
    public void LectureMusique()
    {
        helloController.stopMedia();
        helloController.newMedia(titre, auteur, album, imagePath, songPath, this);
        helloController.playMedia();

        // Apparition du slider
        helloController.sliderDragged();

        // Actualisation de la progressBar
        helloController.actualiseProgressBar();
    }
}