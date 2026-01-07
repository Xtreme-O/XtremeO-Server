package org.example.xtremo.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    ServerSocket server;
    private volatile boolean running = false;
//    int serverPort = Integer.parseInt(ConfigLoader.getProperty("server_port"));
    
    public static Map<Integer,PlayerConnectionHandler> activePlayers = new HashMap<>();

    private final ExecutorService clientPool = Executors.newFixedThreadPool(50);

    public void stop() throws IOException {
        running = false;
        server.close();
        clientPool.shutdown();
    }

    public Server() {
        running = true;
    }

    @Override
    public void run() {
        try {
            this.server = new ServerSocket(6666);
            while (running) {
                Socket player = server.accept();
                clientPool.submit(new PlayerConnectionHandler(player));
                System.getLogger(Server.class.getName()).log(System.Logger.Level.WARNING, "New client has joined");
            }
        } catch (SocketException e) {
            System.getLogger(Server.class.getName()).log(System.Logger.Level.WARNING, e.getLocalizedMessage());
        } catch (IOException e) {
            System.getLogger(Server.class.getName()).log(System.Logger.Level.WARNING, e.getLocalizedMessage());
        }
    }
}
