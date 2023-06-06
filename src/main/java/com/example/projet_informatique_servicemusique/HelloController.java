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
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
    ObservableList<HBox> items = FXCollections.observableArrayList();
    MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File(new File("src/main/resources/Songs/Damso.mp3").getAbsolutePath()).toURI().toString()));
    public static boolean isAdmin = false;

    // Constante : Symbole pause et play
    protected final String svgPathPause = "M 0.7508 0 Q 0 0 0 0.7508 L 0 14.2643 Q 0 15.015 0.7508 15.015 L 2.2523 15.015 Q 3.003 15.015 3.003 14.2643 L 3.003 0.7508 Q 3.003 0 2.2523 0 M 8.2583 0 Q 7.5075 0 7.5075 0.7508 L 7.5075 14.2643 Q 7.5075 15.015 8.2583 15.015 L 9.7598 15.015 Q 10.5105 15.015 10.5105 14.2643 L 10.5105 0.7508 Q 10.5105 0 9.7598 0";
    protected final String svgPathPlay = "M 2.511 0.36 L 11.392 5.562 C 13.293 6.68 13.338 7.932 11.614 8.919 L 2.752 14.031 C 0.985 15.149 -0.066 14.59 -0.056 12.42 L -0.056 1.994 C -0.075 -0.168 0.786 -0.656 2.511 0.36";

    // Méthodes
    public void stopMedia() {
        mediaPlayer.stop();
        setPauseSVG();
    }

    public void pauseMedia() {
        mediaPlayer.pause();
        setPauseSVG();
    }

    public void playMedia() {
        mediaPlayer.play();
        setPlaySVG();
        sli_Timeline.setOpacity(1);
        sli_Timeline.setDisable(false);
    }

    public void newMedia(String Titre, String Auteur, String Album, String Chemin) {
        mediaPlayer = new MediaPlayer(new Media(new File(new File("src/main/resources/Songs/" + Chemin + ".mp3").getAbsolutePath()).toURI().toString()));
        lbl_Titre.setText(Titre);
        lbl_Auteur.setText(Auteur);
        lbl_Album.setText(Album);
    }

    /**
     * Description de la fonction.
     *
     * @param music Description du paramètre 2.
     */

    public void actualiseProgressBar(Music music) {
            //Fonction pour ajuster la progressbar et le slider en fonction du temps
            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
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

            //Conversion des secondes en minutes et secondes
            String formattedRemainingTime = String.format("%02d:%02d", (int)remainingTime.toMinutes(), (int)remainingTime.toSeconds() % 60);
            Duration Time = Duration.millis(mediaPlayer.getCurrentTime().toMillis());
            String RemainingTime = String.format("%02d:%02d", (int)Time.toMinutes(), (int)Time.toSeconds() % 60);

            actualiseTimeLabel(formattedRemainingTime, RemainingTime);
        });
    }

    public void actualiseTimeLabel(String formattedRemainingTime, String RemainingTime) {
        lbl_TempsTot1.setText(formattedRemainingTime);
        lbl_TempsTot.setText(String.valueOf(RemainingTime));
    }

    public void setPlaySVG() {
        svg_Play.setContent(svgPathPause);
    }

    public void setPauseSVG() {
        svg_Play.setContent(svgPathPlay);
    }

    public void actualiseSlider(Duration newValue, double progress, double duration) {
        sli_Timeline.setValue(1-progress);
        sli_Timeline.setValue(newValue.toMillis()/duration);
    }

    public void sliderDragged(Music music) {
        sli_Timeline.setOnDragDetected(dragEvent -> {
            isSliderBeingDragged = true;
        });
    }


    /**
     * Description de la fonction.
     *
     * @param TitreDelete Description du paramètre 1.
     * @param hbox Description du paramètre 2.
     */
    public void SupprimerLigne(String TitreDelete, HBox hbox)
    {
        // Charger les données du fichier CSV dans une liste
        List<String[]> lignes = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(new File("src/main/resources/BaseDonnee.csv").getAbsolutePath()),';')) {
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
            if (ligne[0].equals(TitreDelete)) {
                lignesASupprimer.add(ligne);
            }
        }
        lignes.removeAll(lignesASupprimer);

        // Réécrire le fichier CSV avec les lignes mises à jour
        try (CSVWriter writer = new CSVWriter(new FileWriter(new File("src/main/resources/BaseDonnee.csv").getAbsolutePath()),';')) {
            writer.writeAll(lignes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Supprimer l'instance dans l'Observable list
        items.remove(hbox);
    }

    /**
     * Description de la fonction.
     *
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
    }


    /**
     * Description de la fonction.
     *
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
     * Description de la fonction.
     *
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Music> instances = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(new File("src/main/resources/BaseDonnee.csv").getAbsolutePath()), ';')) {
            String[] ligne;
            while ((ligne = csvReader.readNext()) != null) {
                // Création des objets "Music"
                Music instance = new Music(ligne[0], ligne[1], ligne[2], ligne[3], this);
                instances.add(instance);
                items.add(instance.creerGroupe());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ajouter la ligne d'ajout pour l'admin
        // TODO

        FilteredList<HBox> filteredItems = new FilteredList<>(items);
        SortedList<HBox> sortedList = new SortedList<>(filteredItems);

        chb_TrierPar.getItems().addAll("Titre", "Artiste", "Album");
        chb_TrierPar.setValue("Titre");

        // Tri initial par titre
        sortedList.setComparator(Comparator.comparing(hbox -> {
            VBox vbox = (VBox) hbox.getChildren().get(1);
            TextField titreLabel = (TextField) vbox.getChildren().get(0);
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
                    TextField titreLabel = (TextField) vbox.getChildren().get(0);
                    String titre = titreLabel.getText().toLowerCase();
                    return titre;
                });
            } else if (newValue.equals("Artiste")) {
                comparator = Comparator.comparing(hbox -> {
                    VBox vbox = (VBox) hbox.getChildren().get(1);
                    TextField artisteLabel = (TextField) vbox.getChildren().get(1);
                    String artiste = artisteLabel.getText().toLowerCase();
                    return artiste;
                });
            } else if (newValue.equals("Album")) {
                comparator = Comparator.comparing(hbox -> {
                    TextField albumLabel = (TextField) hbox.getChildren().get(2);
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
        lsv_ListeMusique.setPadding(new Insets(40,20,0,37));
        lsv_ListeMusique.setFixedCellSize(70);

        // C'est pour filter la liste en fonction de la recherche
        txt_Recherche.textProperty().addListener((obs, oldValue, newValue) -> {
            String filterText = txt_Recherche.getText().toLowerCase(); // Critère de filtrage
            filteredItems.setPredicate(hbox -> {
                VBox vbox = (VBox) hbox.getChildren().get(1);
                TextField titreLabel = (TextField)  vbox.getChildren().get(0);
                TextField artisteLabel = (TextField)  vbox.getChildren().get(1);
                TextField albumLabel = (TextField) hbox.getChildren().get(2);
                String titre = titreLabel.getText().toLowerCase();
                String artiste = artisteLabel.getText().toLowerCase();
                String album = albumLabel.getText().toLowerCase();
                return titre.contains(filterText) || artiste.contains(filterText) || album.contains(filterText);
            });
        });
    }

}