package com.example.projet_informatique_servicemusique;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Music {
    // Définition des attributs
    String titre;
    String auteur;
    String album;
    String fichier;
    HelloController helloController;
    HBox groupe = new HBox();

    // Constructeur
    Music(String titre, String auteur, String album, String fichier, HelloController helloController) {
        this.titre = titre;
        this.auteur = auteur;
        this.album = album;
        this.fichier = fichier;
        this.helloController = helloController;
    }

    HBox creerGroupe() {
        // Declaration des objets
        TextField lbl_Titre = new TextField(titre);
        TextField lbl_artiste = new TextField(auteur);
        TextField lbl_album = new TextField(album);
        ImageView image = new ImageView();
        Button btn_Delete = new Button();
        Button btn_Confirm = new Button();

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
        HBox.setHgrow(lbl_album, Priority.ALWAYS);

        // Bloquer la modification de ceci si l'utilisateur n'est pas admin
        if (!HelloController.isAdmin) {
            lbl_Titre.setEditable(false);
            lbl_artiste.setEditable(false);
            lbl_album.setEditable(false);
        }

        // Mettre les boutons de l'admin (supprimer et confirmer)
        if (HelloController.isAdmin) {
            btn_Delete.setText("Delete");
            btn_Confirm.setText("Confirm");
        }
        else {
            btn_Delete.setDisable(true);
            btn_Confirm.setDisable(true);
            btn_Delete.setOpacity(0);
            btn_Confirm.setOpacity(0);
        }

        // Mettre en forme l'image
        String path = "src/main/resources/Pictures/" + fichier + ".png";
        File imageFile = new File(path);
        String localUrl = null;
        try {
            localUrl = imageFile.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        String path2 = "src/main/resources/Pictures/ImagePlay.png";
        File imageFile2 = new File(path2);
        String localUrl2 = null;
        try {
            localUrl2 = imageFile2.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        image.setImage(new Image(localUrl));
        image.setFitHeight(50);
        image.setFitWidth(50);
        image.setStyle("-fx-background-radius: 10px;");
        HBox.setHgrow(image, Priority.NEVER);

        // Préparation de la VBox
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


        // Preparation de la HBox
        groupe.setPadding(new Insets(0,0,0,0));
        groupe.setStyle("-fx-background-radius: 10;");
        groupe.getChildren().addAll(image, titreArtiste, lbl_album, btn_Confirm, btn_Delete);

        groupe.setAlignment(Pos.CENTER);

        // Faire en sorte que le play s'affiche
        String finalLocalUrl2 = localUrl2;
        groupe.setOnMouseEntered(event -> {
            //ImageView image = (ImageView) groupe.getChildren().get(0);
            image.setImage(new Image(finalLocalUrl2)); // Changer l'image au survol
        });



        String finalLocalUrl = localUrl;
        groupe.setOnMouseExited(event -> {
            //ImageView image = (ImageView) groupe.getChildren().get(0);
            image.setImage(new Image(finalLocalUrl)); // Rétablir l'image par défaut
        });

        //Suppression de ligne
        btn_Delete.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
        {
            // Code à exécuter lorsque le bouton est cliqué
            helloController.SupprimerLigne(titre, groupe);
        }
        });


        // Detection du "click"
        groupe.setOnMouseClicked(event -> {
            System.out.println("Élément de la liste cliqué !");
            System.out.println("Titre: " + titre);
            System.out.println("Artiste: " + auteur);
            System.out.println("Album: " + album);

            LectureMusique(titre, auteur, album, fichier);
        });

        /*
        if (helloController.isAdmin) {
            AnchorPane anchorPane = new AnchorPane();
            HBox hBox = new HBox();
            hBox.getChildren().addAll(hBox);
            return hBox;
        }
        */

        return groupe;
    }

    // Création d'objet pour lire la musique

    /**
     * Description de la fonction.
     *
     * @param Titre Description du paramètre 1.
     * @param Auteur Description du paramètre 2.
     * @param Album Description du paramètre 3.
     * @param Chemin Description du paramètre 4.
     */
    public void LectureMusique(String Titre, String Auteur, String Album, String Chemin)
    {
        helloController.stopMedia();
        helloController.newMedia(Titre, Auteur, Album, Chemin);
        helloController.playMedia();

        // Apparition du slider
        helloController.sliderDragged(this);

        // Actualisation de la progressBar
        helloController.actualiseProgressBar(this);
    }
}