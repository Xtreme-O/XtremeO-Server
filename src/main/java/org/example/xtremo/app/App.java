package org.example.xtremo.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private static final String ROOT_FXML = "/org/example/xtremo/view/primary.fxml";
    private static final String THEME_CSS = "/org/example/xtremo/view/theme.css";
    private static final String STYLE_CSS = "/org/example/xtremo/view/style.css";
    private static final String TITLE = "XtremO Server Dashboard";

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML(ROOT_FXML), 1200, 750);
        scene.getStylesheets().add(getClass().getResource(THEME_CSS).toExternalForm());
        scene.getStylesheets().add(getClass().getResource(STYLE_CSS).toExternalForm());
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
