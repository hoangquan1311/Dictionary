module com.example.dictionaryApplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires freetts;
    requires java.net.http;

    opens com.example.Dictionary to javafx.fxml;
    exports com.example.Dictionary;
}