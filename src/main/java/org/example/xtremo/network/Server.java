package org.example.xtremo.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.example.xtremo.logging.LoggerManager;
import org.example.xtremo.session.SessionManager;

public class Server implements Runnable {

    private ServerSocket serverSocket;
    private volatile boolean running = true;

    public static final ConcurrentHashMap<Integer, PlayerConnectionHandler> activePlayers
            = new ConcurrentHashMap<>();

    public static final SessionManager sessionManager = SessionManager.getManager();
    public static final LoggerManager logger = LoggerManager.getInstance();

    private final ExecutorService clientPool = Executors.newFixedThreadPool(50);

    @Override
    public void run() {
        try {
            int ServerPort = ServerConfig.getServerPortNumber();
            serverSocket = new ServerSocket(ServerPort);
            logger.info("Server started on port: " + ServerPort);

            while (running) {
                Socket socket = serverSocket.accept();
                PlayerConnectionHandler handler = new PlayerConnectionHandler(socket);
                clientPool.submit(handler);
                logger.info("New client connected: " + socket.getRemoteSocketAddress());
            }
        } catch (IOException e) {
            if (running) {
                logger.error("Server error: " + e.getMessage());
            }
        }
    }

    public void stop() throws IOException {
        Server.activePlayers.forEachValue(1, PlayerConnectionHandler::forceDisconnect);
        running = false;
        serverSocket.close();
        clientPool.shutdownNow();
    }
}
