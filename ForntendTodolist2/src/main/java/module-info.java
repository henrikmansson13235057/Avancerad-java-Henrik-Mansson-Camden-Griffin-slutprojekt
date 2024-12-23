module com.example.forntendtodolist {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires com.google.gson;
    requires org.apache.httpcomponents.core5.httpcore5;

    opens com.example.forntendtodolist2 to javafx.fxml;
    exports com.example.forntendtodolist2;
}


