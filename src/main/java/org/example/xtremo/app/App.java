package org.example.xtremo.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import org.example.xtremo.network.Server;

public class App extends Application {
    
    private Thread serverThread;
    private Server server;

    private static Scene scene;
    private static final String ROOT_FXML = "/org/example/xtremo/view/primary.fxml";
    private static final String THEME_CSS = "/org/example/xtremo/view/theme.css";
    private static final String STYLE_CSS = "/org/example/xtremo/view/style.css";
    private static final String TITLE = "XtremO Server Dashboard";

    @Override
    public void start(Stage stage) throws IOException {
        server = new Server();
        serverThread = new Thread(server, "Server-Main-Thread");
        
        serverThread.start();

        stage.setOnCloseRequest(e -> {
            try {
                server.stop();
            } catch (IOException ex) {
                System.getLogger(App.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
        
        scene = new Scene(loadFXML(ROOT_FXML), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
       server.stop();
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
