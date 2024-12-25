module com.example.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.formsfx;
    requires com.google.gson;
    opens com.example.frontend to com.google.gson, javafx.fxml;
    exports com.example.frontend;
}