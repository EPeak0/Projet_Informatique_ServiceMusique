package com.example.projet_informatique_servicemusique;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    protected ProgressBar psb_Timeline;
    @FXML
    protected Slider sli_Timeline;
    @FXML
    protected ListView lsv_ListeMusique;
    @FXML
    protected TextField txt_Recherche;

    // Permet de creer 1 ligne pour la liste
    public HBox creerGroupe(String titre, String artiste, String album, String nomImage) {
        // Declaration des objets
        Label lbl_Titre = new Label(titre);
        Label lbl_artiste = new Label(artiste);
        Label lbl_album = new Label(album);
        ImageView image = new ImageView();

        // Definition de la police d'écriture
        Font fontBig = Font.font("Arial Rounded MT Bold", 18);
        Font font = Font.font("Arial Rounded MT Bold");

        // Mise en forme des labels
        lbl_Titre.setTextFill(Color.WHITE);
        lbl_Titre.setFont(fontBig);
        lbl_Titre.setPadding(new Insets(0,0,0,20));
        lbl_Titre.setAlignment(Pos.CENTER_LEFT);

        lbl_artiste.setTextFill(Color.rgb(121,121,121));
        lbl_artiste.setFont(font);
        lbl_artiste.setPadding(new Insets(0,0,0,20));
        lbl_artiste.setAlignment(Pos.CENTER_LEFT);

        lbl_album.setTextFill(Color.WHITE);
        lbl_album.setFont(font);
        lbl_album.setPadding(new Insets(0,10,0,0));
        lbl_album.setAlignment(Pos.CENTER);
        HBox.setHgrow(lbl_album, Priority.NEVER);

        // Mettre en forme l'image
        image.setImage(new Image("C:\\Users\\diemo\\Pictures\\Background\\Image3.jpg"));
        image.setFitHeight(50);
        image.setFitWidth(50);
        HBox.setHgrow(image, Priority.NEVER);

        // Préparation de la VBox
        VBox titreArtiste = new VBox();
        titreArtiste.setPadding(new Insets(0,0,0,0));
        titreArtiste.getChildren().addAll(lbl_Titre, lbl_artiste);
        titreArtiste.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(titreArtiste, Priority.NEVER);

        // Préparation de la séparation
        Separator separator = new Separator();
        HBox.setHgrow(separator, Priority.ALWAYS);
        separator.setFocusTraversable(false);
        separator.setVisible(false);

        // Preparation de la HBox
        HBox groupe = new HBox();
        groupe.setPadding(new Insets(0,0,0,0));
        groupe.setStyle("-fx-background-radius: 10;");
        groupe.getChildren().addAll(image, titreArtiste, separator, lbl_album);
        groupe.setAlignment(Pos.CENTER);

        // Faire en sorte que le play s'affiche
        groupe.setOnMouseEntered(event -> {
            //ImageView image = (ImageView) groupe.getChildren().get(0);
            image.setImage(new Image("C:\\Users\\diemo\\Pictures\\Background\\Image2.jpg")); // Changer l'image au survol
        });

        groupe.setOnMouseExited(event -> {
            //ImageView image = (ImageView) groupe.getChildren().get(0);
            image.setImage(new Image("C:\\Users\\diemo\\Pictures\\Background\\Image3.jpg")); // Rétablir l'image par défaut
        });

        // Detection du "click"
        groupe.setOnMouseClicked(event -> {
            System.out.println("Élément de la liste cliqué !");
            System.out.println("Titre: " + titre);
            System.out.println("Artiste: " + artiste);
            System.out.println("Album: " + album);
        });

        return groupe;
    }

    @FXML
    protected void btn_Play_Click() {

    }
    @FXML
    protected void btn_Next_Click() {

    }
    @FXML
    protected void btn_Previous_Click() {

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<HBox> items = FXCollections.observableArrayList();
        items.add(creerGroupe("Groupe 1", "artiste example","album example", "Image1.jpg"));
        items.add(creerGroupe("Groupe 2", "7777777777777zzzz","2eftgwg4", "Image1.jpg"));
        items.add(creerGroupe("Groupe 2", "7777777777777zzzz","2eftgwg4", "Image1.jpg"));
        items.add(creerGroupe("Groupe 2", "7777777777777zzzz","2eftgwg4", "Image1.jpg"));
        items.add(creerGroupe("Groupe 2", "7777777777777zzzz","2eftgwg4", "Image1.jpg"));
        items.add(creerGroupe("Groupe 2", "7777777777777zzzz","2eftgwg4", "Image1.jpg"));
        items.add(creerGroupe("Groupe 2", "7777777777777zzzz","2eftgwg4", "Image1.jpg"));
        items.add(creerGroupe("Groupe 2", "7777777777777zzzz","2eftgwg4", "Image1.jpg"));
        items.add(creerGroupe("Groupe 2", "7777777777777zzzz","2eftgwg4", "Image1.jpg"));

        FilteredList<HBox> filteredItems = new FilteredList<>(items);
        lsv_ListeMusique.setItems(filteredItems);
        lsv_ListeMusique.setCellFactory(param -> new NoMarginListCell());
        lsv_ListeMusique.setPadding(new Insets(20,20,20,37));
        lsv_ListeMusique.setFixedCellSize(70);

        // C'est pour filter la liste en fonction de la recherche
        txt_Recherche.textProperty().addListener((obs, oldValue, newValue) -> {
            String filterText = txt_Recherche.getText().toLowerCase(); // Critère de filtrage
            filteredItems.setPredicate(hbox -> {
                VBox vbox = (VBox) hbox.getChildren().get(1);
                Label titreLabel = (Label)  vbox.getChildren().get(0);
                Label artisteLabel = (Label)  vbox.getChildren().get(1);
                Label albumLabel = (Label) hbox.getChildren().get(3);
                String titre = titreLabel.getText().toLowerCase();
                String artiste = artisteLabel.getText().toLowerCase();
                String album = albumLabel.getText().toLowerCase();
                return titre.contains(filterText) || artiste.contains(filterText) || album.contains(filterText);
            });
        });
    }

    private class NoMarginListCell extends ListCell<HBox> {
        @Override
        protected void updateItem(HBox item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(item);
            setPadding(new javafx.geometry.Insets(0));
            setStyle("-fx-background-radius: 10; -fx-background-color: rgb(0,0,0,0);");
        }
    }
}