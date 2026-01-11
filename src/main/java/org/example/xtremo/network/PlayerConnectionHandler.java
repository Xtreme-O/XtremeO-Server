package org.example.xtremo.network;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import static org.example.xtremo.network.Server.logger;
import org.example.xtremo.session.Session;

/**
 *
 * @author wahid
 */
public class PlayerConnectionHandler implements Runnable {

    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;

    private int playerId = -1;

    public PlayerConnectionHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
    }

    public Socket getSocket() {
        return socket;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
        Server.activePlayers.put(playerId, this);
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                String msg = dis.readUTF();
                JsonObject root = JsonParser.parseString(msg).getAsJsonObject();
                PlayerNetworkOperations.handleClientActionRequest(root, this);
            }
        } catch (EOFException | SocketException e) {
            handleDisconnect();
        } catch (Exception e) {
            logger.error("Server error: " + e.getMessage());
            handleDisconnect();
        }
    }

    public void forceDisconnect() {
        logger.info("Force disconnect initiated for playerId=" + playerId);
        handleDisconnect();
    }

    private void handleDisconnect() {
        if (playerId != -1) {
            logger.info("Disconnecting playerId=" + playerId);
            Server.activePlayers.remove(playerId);
            Session session = Server.sessionManager.getByPlayer(playerId);
            if (session != null) {
                logger.info("Removing session for playerId=" + playerId);
                session.onPlayerDisconnected(playerId);
                Server.sessionManager.remove(session);
            } else {
                logger.warn("No active session found for playerId=" + playerId);
            }
        }
        try {
            if (!socket.isClosed()) {
                socket.close();
                logger.info("Socket closed for playerId=" + playerId);
            }
        } catch (IOException e) {
            logger.error("Error closing socket for playerId=" + playerId + ": " + e.getMessage());
        }
    }
}