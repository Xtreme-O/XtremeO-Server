module org.example.xtremo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    opens org.example.xtremo.network.protocol to com.google.gson;
    opens org.example.xtremo.network.protocol.models to com.google.gson;
    opens org.example.xtremo.model.dto to com.google.gson;
    opens org.example.xtremo.model.enums to com.google.gson;
    
    opens org.example.xtremo.controller to javafx.fxml;
    opens org.example.xtremo.view to javafx.fxml;

    exports org.example.xtremo.app;
}
