module com.example.projet_informatique_servicemusique {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.projet_informatique_servicemusique to javafx.fxml;
    exports com.example.projet_informatique_servicemusique;
}