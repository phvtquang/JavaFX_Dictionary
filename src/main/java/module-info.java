module com.example.dictionaryApplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires freetts;

    opens com.example.dictionaryApplication to javafx.fxml;
    exports com.example.dictionaryApplication;
}