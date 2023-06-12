package com.example.projet_informatique_servicemusique;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.Button;


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
    @FXML
    protected Button btn_Ajouter;
    @FXML
    protected ImageView img_currentImage;
    protected boolean isSliderBeingDragged = false;

    // Déclaration des listes
    ObservableList<HBox> items = FXCollections.observableArrayList();
    FilteredList<HBox> filteredItems = new FilteredList<>(items);
    SortedList<HBox> sortedList = new SortedList<>(filteredItems);

    // Déclaration des valeurs de la musique en cours de lecture
    String currentTitre;
    String currentArtiste;
    String currentAlbum;
    String currentImagePath;
    String currentSongPath;
    Music currentMusic;

    // Déclaration du mediaPlayer
    MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File(new File("src/main/resources/Songs/Damso.mp3").getAbsolutePath()).toURI().toString()));

    // Indique si l'utilisateur est un administateur
    public static boolean isAdmin = false;

    // Constante : Symbole pause et play
    protected final String svgPathPause = "M 0.7508 0 Q 0 0 0 0.7508 L 0 14.2643 Q 0 15.015 0.7508 15.015 L 2.2523 15.015 Q 3.003 15.015 3.003 14.2643 L 3.003 0.7508 Q 3.003 0 2.2523 0 M 8.2583 0 Q 7.5075 0 7.5075 0.7508 L 7.5075 14.2643 Q 7.5075 15.015 8.2583 15.015 L 9.7598 15.015 Q 10.5105 15.015 10.5105 14.2643 L 10.5105 0.7508 Q 10.5105 0 9.7598 0";
    protected final String svgPathPlay = "M 2.511 0.36 L 11.392 5.562 C 13.293 6.68 13.338 7.932 11.614 8.919 L 2.752 14.031 C 0.985 15.149 -0.066 14.59 -0.056 12.42 L -0.056 1.994 C -0.075 -0.168 0.786 -0.656 2.511 0.36";

    // Constantes chemins de fichier
    final String imagesFolder = "src/main/resources/Pictures/";
    final String songFolder = "src/main/resources/Songs/";
    final String csvDataPath = "src/main/resources/BaseDonnee.csv";

    /**
     * Méthode pour mettre le mediaPlayer en arret
     */
    public void stopMedia() {
        mediaPlayer.stop();
        setPauseSVG();

        // Retirer la couleur du titre dans la liste
        if (!(currentMusic == null)) {
            currentMusic.lbl_Titre.setStyle("-fx-text-fill: #FFFFFF");
        }
    }

    /**
     * Méthode pour mettre le mediaPlayer en pause
     */
    public void pauseMedia() {
        mediaPlayer.pause();
        setPauseSVG();
    }

    /**
     * Méthode pour mettre le mediaPlayer en play
     */
    public void playMedia() {
        mediaPlayer.play();
        setPlaySVG();

        // Faire apparaitre le slider
        sli_Timeline.setOpacity(1);
        sli_Timeline.setDisable(false);

        // Mettre la couleur du titre dans la liste
        currentMusic.lbl_Titre.setStyle("-fx-text-fill: #FF8000");
    }

    // Méthode permettant d'assigner un nouveau média au mediaPlayer
    public void newMedia(String titre, String auteur, String album, String imagePath, String songPath, Music music) {
        // Définition des attributs
        currentTitre = titre;
        currentArtiste = auteur;
        currentAlbum = album;
        currentImagePath = imagePath;
        currentSongPath = songPath;
        currentMusic = music;

        // Mise de la nouvelle musique de le mediaPlayer
        mediaPlayer = new MediaPlayer(new Media(new File(new File(songFolder + songPath).getAbsolutePath()).toURI().toString()));

        // Mettre à jour le panneau indicateur
        lbl_Titre.setText(titre);
        lbl_Auteur.setText(auteur);
        lbl_Album.setText(album);

        // Préparation de l'image à mettre dans le panneau indicateur
        String path = imagesFolder + imagePath;
        File imageFile = new File(path);
        String localUrl = null;
        try {
            localUrl = imageFile.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // Mise de l'image de la musique en cours de lecture dans le panneau indicateur
        img_currentImage.setImage(new Image(localUrl));

        // Détecte la fin de la musique. On met le mediaPlayer en arrêt lorsque la musique est arrivé au bout
        mediaPlayer.setOnEndOfMedia(() -> {
            stopMedia();
        });
    }

    /**
     * Fonction permettant de faire avancer le slider et la progressBar.
     * Permet aussi le déplacement du slider à la main. (désactive l'actualisation lorsque le slider est déplacé)
     */
    public void actualiseProgressBar() {
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> { //Fonction pour ajuster le progressbarre en fonction du temps
            double duration = mediaPlayer.getTotalDuration().toMillis();
            double remaining = duration - mediaPlayer.getCurrentTime().toMillis();
            double progress = remaining / duration;

            if (!isSliderBeingDragged) {
                actualiseSlider(newValue, progress, duration);
            }

            sli_Timeline.setOnMouseReleased(event -> {
                isSliderBeingDragged = false;
                mediaPlayer.seek(Duration.millis((sli_Timeline.getValue())*duration)); //Fonction pour faire avancer le slider et donc la musique
            });

            Duration remainingTime = Duration.millis(remaining);
            String formattedRemainingTime = String.format("%02d:%02d", (int)remainingTime.toMinutes(), (int)remainingTime.toSeconds() % 60);

            Duration Time = Duration.millis(mediaPlayer.getCurrentTime().toMillis());
            String RemainingTime = String.format("%02d:%02d", (int)Time.toMinutes(), (int)Time.toSeconds() % 60);

            actualiseTimeLabel(formattedRemainingTime, RemainingTime);
        });
    }

    /**
     * Actualise le temps restant et le temps écoulé
     * @param formattedRemainingTime
     * @param RemainingTime
     */
    public void actualiseTimeLabel(String formattedRemainingTime, String RemainingTime) {
        lbl_TempsTot1.setText(formattedRemainingTime);
        lbl_TempsTot.setText(String.valueOf(RemainingTime));
    }

    /**
     * Permet d'afficher le logo pause dans le bouton de commande
     */
    public void setPlaySVG() {
        svg_Play.setContent(svgPathPause);
    }

    /**
     * Permet d'afficher le logo play dans le bouton de commande
     */
    public void setPauseSVG() {
        svg_Play.setContent(svgPathPlay);
    }

    /**
     * Permet d'actualiser la position du slider en fonction des valeurs en entrée
     * @param newValue
     * @param progress
     * @param duration
     */
    public void actualiseSlider(Duration newValue, double progress, double duration) {
        sli_Timeline.setValue(1-progress);
        sli_Timeline.setValue(newValue.toMillis()/duration);
    }

    /**
     * Permet de detecter si le slider est bougé manuellement
     */
    public void sliderDragged() {
        sli_Timeline.setOnMousePressed(mouseEvent -> {
            isSliderBeingDragged = true;
        });
        sli_Timeline.setOnDragDetected(dragEvent -> {
            isSliderBeingDragged = true;
        });
    }

    /**
     * Méthode permettant de supprimer une musique dans le csv et de la faire disparaitre de la listView
     * @param TitreDelete       Titre à supprimer
     * @param ArtisteDelete     Artiste à supprimer
     * @param AlbumDelete       Album à supprimer
     * @param imageName         Nom du fichier de l'image à supprimer
     * @param songName          Nom du fichier de la musique à supprimer
     * @param hbox              HBox contenant la ligne représentant la musique
     * @param deleteImageFile   Valeur Bool. TRUE : suprimer le fichier iamge
     * @param deleteSongFile    Valeur Bool. TRUE : supprimer le fichier musique
     */
    public void SupprimerLigne(String TitreDelete, String ArtisteDelete, String AlbumDelete, String imageName, String songName, HBox hbox, Boolean deleteImageFile, Boolean deleteSongFile)
    {
        // Charger les données du fichier CSV dans une liste
        List<String[]> lignes = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(new File(csvDataPath).getAbsolutePath()),';')) {
            String[] ligne;
            while ((ligne = reader.readNext()) != null) {
                lignes.add(ligne);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Supprimer les lignes correspondantes
        List<String[]> lignesASupprimer = new ArrayList<>();
        for (String[] ligne : lignes) {
            if (ligne[0].equals(TitreDelete) && ligne[1].equals(ArtisteDelete) && ligne[2].equals(AlbumDelete) && ligne[3].equals(imageName) && ligne[4].equals(songName)) {
                lignesASupprimer.add(ligne);
            }
        }
        lignes.removeAll(lignesASupprimer);

        // Réécrire le fichier CSV avec les lignes mises à jour
        try (CSVWriter writer = new CSVWriter(new FileWriter(new File(csvDataPath).getAbsolutePath()),';')) {
            writer.writeAll(lignes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Suppression du fichier de l'image de la pochette
        if (deleteImageFile) {
            File imageFile = new File(imagesFolder + imageName);

            if (imageFile.exists()) {
                imageFile.delete(); // Retourne une valeur bool
            }
        }

        // Suppression du fichier de l'image de la pochette
        if (deleteSongFile) {
            File songFile = new File(songFolder + songName);

            if (songFile.exists()) {
                songFile.delete(); // Retourne une valeur bool
            }
        }

        items.remove(hbox);
    }

    /**
     * Ajoute une nouvelle entrée dans la listView et crée un nouvelle musique
     * @param titre         Spécifie le titre
     * @param artiste       Spécifie l'artiste
     * @param album         Spécifie l'album
     * @param imageName     Spécifie le nom du fichier de l'image
     * @param songName      Spécifie le nom du fichier de la musique
     */
    public void refreshList(String titre, String artiste, String album, String imageName, String songName) {
        //  Création d'une nouvelle instance de musique
        Music music = new Music(titre, artiste, album, imageName, songName, this);
        items.add(music.creerGroupe());
    }

    /**
     * Lorsque le bouton Play/Pause est pressé, on fait pause/play
     */
    @FXML
    protected void btn_Play_Click()
    {
        if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
        {
            System.out.println("Musique en pause");
            pauseMedia();
        }

        else if(mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED)
        {
            System.out.println("Musique qui repart");
            playMedia();
        }
        else {
            newMedia(currentTitre, currentArtiste, currentAlbum, currentImagePath, currentSongPath, currentMusic);
            playMedia();

            // Apparition du slider
            sliderDragged();

            // Actualisation de la progressBar
            actualiseProgressBar();
        }
    }

    /**
     * On affiche la page "login-view"
     * @throws IOException
     */
    @FXML
    protected void btn_LogOut_Click() throws IOException {
        // Arret de la lecture du média
        stopMedia();

        isAdmin = false;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene newScene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) lbl_Titre.getScene().getWindow();
        stage.setTitle("TDT - Login");
        stage.setScene(newScene);
        stage.setResizable(false);

        stage.setMinWidth(0);
        stage.setMinHeight(0);
        stage.setWidth(400);
        stage.setHeight(450);

        stage.show();
    }

    /**
     * Lorsque on appuie sur le bouton "ajouter", on affiche la page "add-view"
     * @throws IOException
     */
    @FXML
    public void btn_Ajouter_Click() throws IOException {

        Stage primaryStage = (Stage) lbl_Titre.getScene().getWindow();

        // Créer une nouvelle fenêtre pop-up

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-view.fxml"));
        Scene newScene = new Scene(fxmlLoader.load());

        // Passer cette instance à la vue
        AddController addController = fxmlLoader.getController();
        addController.setHelloController(this);

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(primaryStage);
        popupStage.initStyle(StageStyle.UTILITY);
        popupStage.setTitle("TDT - add a song");
        popupStage.setScene(newScene);

        popupStage.setResizable(false);
        popupStage.setWidth(800);
        popupStage.setHeight(450);

        popupStage.showAndWait();
    }

    /**
     * Méthode s'exécutant lors de l'initialisation de cette page
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Rend visible ou pas le bouton "ajouter lignes"
        if (isAdmin){
            btn_Ajouter.setDisable(false);
            btn_Ajouter.setOpacity(1);
        }
        else {
            btn_Ajouter.setDisable(true);
            btn_Ajouter.setOpacity(0);
        }

        ArrayList<Music> instances = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(new File(csvDataPath).getAbsolutePath()), ';')) {
            String[] ligne;
            while ((ligne = csvReader.readNext()) != null) {
                // Création des objets "Music"
                Music instance = new Music(ligne[0], ligne[1], ligne[2], ligne[3], ligne[4], this);
                instances.add(instance);
                items.add(instance.creerGroupe());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        lsv_ListeMusique.setCellFactory(param -> new ListCell<HBox>() {
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);

                setMaxWidth(lsv_ListeMusique.getWidth()); // Définir la largeur maximale sur la largeur de la ListView
                setGraphic(item);
                setPadding(new javafx.geometry.Insets(0));
                setStyle("-fx-background-radius: 10; -fx-background-color: rgb(0,0,0,0);");
                setMaxWidth(lsv_ListeMusique.getWidth()); // Définir la largeur maximale sur la largeur de la ListView
            }
        });

        //lsv_ListeMusique.setCellFactory(param -> new NoMarginListCell());
        lsv_ListeMusique.setPadding(new Insets(40,20,0,27));
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
}