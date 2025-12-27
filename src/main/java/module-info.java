module org.example.xtremo {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.xtremo to javafx.fxml;
    exports org.example.xtremo;
}
