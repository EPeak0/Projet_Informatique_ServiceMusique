package com.example.projet_informatique_servicemusique;

import com.opencsv.CSVReader;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.util.Duration;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
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
    @FXML
    protected Label lbl_TempsTot;
    @FXML
    protected Label lbl_TempsTot1;
    @FXML
    protected Label lbl_Titre;
    @FXML
    protected Label lbl_Auteur;
    @FXML
    protected Label lbl_Album;
    @FXML
    protected ComboBox chb_TrierPar;
    @FXML
    protected SVGPath svg_Play;
    protected boolean isSliderBeingDragged = false;

    // Constante : Symbole pause et play
    protected final String svgPathPause = "M 0.7508 0 Q 0 0 0 0.7508 L 0 14.2643 Q 0 15.015 0.7508 15.015 L 2.2523 15.015 Q 3.003 15.015 3.003 14.2643 L 3.003 0.7508 Q 3.003 0 2.2523 0 M 8.2583 0 Q 7.5075 0 7.5075 0.7508 L 7.5075 14.2643 Q 7.5075 15.015 8.2583 15.015 L 9.7598 15.015 Q 10.5105 15.015 10.5105 14.2643 L 10.5105 0.7508 Q 10.5105 0 9.7598 0";
    protected final String svgPathPlay = "M 2.511 0.36 L 11.392 5.562 C 13.293 6.68 13.338 7.932 11.614 8.919 L 2.752 14.031 C 0.985 15.149 -0.066 14.59 -0.056 12.42 L -0.056 1.994 C -0.075 -0.168 0.786 -0.656 2.511 0.36";

    // Permet de creer 1 ligne pour la liste
    public HBox creerGroupe(String titre, String artiste, String album, String Chemin) {
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
        lbl_album.setPadding(new Insets(0,0,0,0));
        lbl_album.setAlignment(Pos.CENTER_LEFT);
        lbl_album.setPrefWidth(700);
        HBox.setHgrow(lbl_album, Priority.ALWAYS);

        // Mettre en forme l'image
        String path = "src/main/resources/Pictures/" + Chemin + ".png";
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
        titreArtiste.setPrefWidth(400);
        titreArtiste.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(titreArtiste, Priority.ALWAYS);

        // Préparation des séparations
        Separator separator = new Separator();
        HBox.setHgrow(separator, Priority.ALWAYS);
        separator.setFocusTraversable(false);
        separator.setVisible(false);

        // Preparation de la HBox
        HBox groupe = new HBox();
        groupe.setPadding(new Insets(0,0,0,0));
        groupe.setStyle("-fx-background-radius: 10;");
        groupe.getChildren().addAll(image, titreArtiste, lbl_album);
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

        // Detection du "click"
        groupe.setOnMouseClicked(event -> {
            System.out.println("Élément de la liste cliqué !");
            System.out.println("Titre: " + titre);
            System.out.println("Artiste: " + artiste);
            System.out.println("Album: " + album);

            LectureMusique(titre, artiste, album, Chemin);
        });

        return groupe;
    }

    //Création d'objet pour lire la musique
    String music = new File("src/main/resources/Songs/Damso.mp3").getAbsolutePath();
    Media media = new Media(new File(music).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);

    public void LectureMusique(String Titre, String Auteur, String Album, String Chemin)
    {
        mediaPlayer.stop();
        music = new File("src/main/resources/Songs/" + Chemin + ".mp3").getAbsolutePath();
        media = new Media(new File(music).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        svg_Play.setContent(svgPathPause);

        // Actualisation du panneau indicateur
        lbl_Titre.setText(Titre);
        lbl_Auteur.setText(Auteur);
        lbl_Album.setText(Album);

        // Apparition du slider
        sli_Timeline.setOpacity(1);
        sli_Timeline.setDisable(false);

        sli_Timeline.setOnDragDetected(dragEvent -> {
            isSliderBeingDragged = true;
        });

        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> { //Fonction pour ajuster le progressbarre en fonction du temps
            double duration = mediaPlayer.getTotalDuration().toMillis();
            double remaining = duration - mediaPlayer.getCurrentTime().toMillis();
            double progress = remaining / duration;

            if (!isSliderBeingDragged) {
                sli_Timeline.setValue(1-progress);
                sli_Timeline.setValue(newValue.toMillis()/duration);
            }

            sli_Timeline.setOnMouseReleased(event -> {
                isSliderBeingDragged = false;
                mediaPlayer.seek(Duration.millis((sli_Timeline.getValue())*duration)); //Fonction pour faire avancer le slider et donc la musique
            });

            Duration remainingTime = Duration.millis(remaining);
            String formattedRemainingTime = String.format("%02d:%02d", (int)remainingTime.toMinutes(), (int)remainingTime.toSeconds() % 60);
            lbl_TempsTot1.setText(formattedRemainingTime);

            Duration Time = Duration.millis(mediaPlayer.getCurrentTime().toMillis());
            String RemainingTime = String.format("%02d:%02d", (int)Time.toMinutes(), (int)Time.toSeconds() % 60);
            lbl_TempsTot.setText(String.valueOf(RemainingTime));
        });

    }
    @FXML
    protected void btn_Play_Click()
    {
        if(mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING && mediaPlayer.getStatus() != MediaPlayer.Status.PAUSED)
        {
            mediaPlayer.play();
            System.out.println("Musique qui se lance");
            System.out.println(mediaPlayer.getStatus());
            svg_Play.setContent(svgPathPause);
        }

        else if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
        {
            System.out.println("Musique en pause");
            mediaPlayer.pause();
            svg_Play.setContent(svgPathPlay);
        }

        else if(mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED)
        {
            System.out.println("Musique qui repart");
            mediaPlayer.play();
            svg_Play.setContent(svgPathPause);
        }

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
        try (CSVReader csvReader = new CSVReader(new FileReader(new File("src/main/resources/BaseDonnee.csv").getAbsolutePath()), ';')) {
            String[] ligne;
            while ((ligne = csvReader.readNext()) != null) {
                items.add(creerGroupe(ligne[0],ligne[1],ligne[2],ligne[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        FilteredList<HBox> filteredItems = new FilteredList<>(items);
        SortedList<HBox> sortedList = new SortedList<>(filteredItems);

        chb_TrierPar.getItems().addAll("Titre", "Artiste", "Album");
        chb_TrierPar.setValue("Titre");

        // Tri initial par titre
        sortedList.setComparator(Comparator.comparing(hbox -> {
            VBox vbox = (VBox) hbox.getChildren().get(1);
            Label titreLabel = (Label) vbox.getChildren().get(0);
            String titre = titreLabel.getText().toLowerCase();
            return titre;
        }));
        lsv_ListeMusique.setItems(sortedList);

        chb_TrierPar.valueProperty().addListener((observable, oldValue, newValue) -> {
            Comparator<HBox> comparator = null;

            // Vérifier la valeur sélectionnée et définir le comparateur en conséquence
            if (newValue.equals("Titre")) {
                comparator = Comparator.comparing(hbox -> {
                    VBox vbox = (VBox) hbox.getChildren().get(1);
                    Label titreLabel = (Label) vbox.getChildren().get(0);
                    String titre = titreLabel.getText().toLowerCase();
                    return titre;
                });
            } else if (newValue.equals("Artiste")) {
                comparator = Comparator.comparing(hbox -> {
                    VBox vbox = (VBox) hbox.getChildren().get(1);
                    Label artisteLabel = (Label) vbox.getChildren().get(1);
                    String artiste = artisteLabel.getText().toLowerCase();
                    return artiste;
                });
            } else if (newValue.equals("Album")) {
                comparator = Comparator.comparing(hbox -> {
                    Label albumLabel = (Label) hbox.getChildren().get(2);
                    String album = albumLabel.getText().toLowerCase();
                    return album;
                });
            }

            // Mettre à jour le comparateur de la SortedList
            sortedList.setComparator(comparator);
        });

        // Mettre à jour la FilteredList lorsque la SortedList change
        sortedList.addListener((ListChangeListener<HBox>) change -> {
            lsv_ListeMusique.setItems(sortedList);
        });

        lsv_ListeMusique.setCellFactory(param -> new NoMarginListCell());
        lsv_ListeMusique.setPadding(new Insets(40,20,0,37));
        lsv_ListeMusique.setFixedCellSize(70);

        // C'est pour filter la liste en fonction de la recherche
        txt_Recherche.textProperty().addListener((obs, oldValue, newValue) -> {
            String filterText = txt_Recherche.getText().toLowerCase(); // Critère de filtrage
            filteredItems.setPredicate(hbox -> {
                VBox vbox = (VBox) hbox.getChildren().get(1);
                Label titreLabel = (Label)  vbox.getChildren().get(0);
                Label artisteLabel = (Label)  vbox.getChildren().get(1);
                Label albumLabel = (Label) hbox.getChildren().get(2);
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