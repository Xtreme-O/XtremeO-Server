module org.example.xtremo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;

    
    opens org.example.xtremo.controller to javafx.fxml;
    opens org.example.xtremo.view to javafx.fxml;

    exports org.example.xtremo.app;
}
