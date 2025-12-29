module org.example.xtremo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires atlantafx.base;
    requires animatefx;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    
    opens org.example.xtremo.controller to javafx.fxml;
    exports org.example.xtremo.app;
}
