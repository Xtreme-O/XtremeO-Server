package org.example.xtremo.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server implements Runnable{
    
    ServerSocket server;
    private volatile boolean running = true;
//    int serverPort = Integer.parseInt(ConfigLoader.getProperty("server_port"));
    public static Vector<PlayerConnectionHandler> players = new Vector<>();
    
    private ExecutorService clientPool = Executors.newFixedThreadPool(50);
    
    public void stop() throws IOException {
        running = false;
        server.close();
        clientPool.shutdown();
    }
    
    
    public Server(){
        
    }

    @Override
    public void run() {
        try {
            this.server = new ServerSocket(6666);
            while(running){
                Socket player = server.accept();
                clientPool.submit(new PlayerConnectionHandler(player));
                System.getLogger(Server.class.getName()).log(System.Logger.Level.INFO,"New client has joined");
//                System.out.println("New client has joined");
            }
        } catch (IOException ex) {
            System.getLogger(Server.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
