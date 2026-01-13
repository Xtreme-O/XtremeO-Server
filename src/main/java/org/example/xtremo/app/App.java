package org.example.xtremo.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import org.example.xtremo.network.Server;
import org.example.xtremo.ui.Screen;

public class App extends Application {
    
    private Thread serverThread;
    private Server server;

    private static Scene scene;
  

    @Override
    public void start(Stage stage) throws IOException {
        
        server = new Server();
        serverThread = new Thread(server, "Server-Main-Thread");
        
        serverThread.start();

        stage.setOnCloseRequest(e -> {
            try {
                server.shutdown();
            } catch (IOException ex) {
                System.getLogger(App.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
        
        scene = initializeScene();
        stage.setTitle(Screen.TITLE);
        stage.setScene(scene);
        stage.show();
    }
    
    private Scene initializeScene() throws IOException{
        scene = new Scene(loadFXML(Screen.ROOT_FXML), 1200, 750);
        scene.getStylesheets().add(getClass().getResource(Screen.THEME_CSS).toExternalForm());
        scene.getStylesheets().add(getClass().getResource(Screen.STYLE_CSS).toExternalForm());
        return scene;
    }

    @Override
    public void stop() throws Exception {
       server.shutdown();
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
