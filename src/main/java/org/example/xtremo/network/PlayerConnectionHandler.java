package org.example.xtremo.network;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

/**
 *
 * @author wahid
 */
public class PlayerConnectionHandler implements Runnable {

    private final Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getDis() {
        return dis;
    }

    public DataOutputStream getDos() {
        return dos;
    }
    
    public PlayerConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(socket.getInputStream()); DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

            socket.setTcpNoDelay(true);
            socket.setKeepAlive(true);
            socket.setSoTimeout(0);
            this.dis = dis;
            this.dos = dos;

            while (!socket.isClosed()) {
                String message;
                try {
                    message = dis.readUTF();
                    dos.flush();
                } catch (EOFException eof) {
                    System.getLogger(PlayerConnectionHandler.class.getName())
                            .log(System.Logger.Level.INFO, "Client disconnected (EOF): {0}", socket.getRemoteSocketAddress());
                    break;
                } catch (SocketException se) {
                    System.getLogger(PlayerConnectionHandler.class.getName())
                            .log(System.Logger.Level.WARNING, "Socket exception: {0}", se.getMessage());
                    break;
                }
                JsonObject root = JsonParser.parseString(message).getAsJsonObject();
                System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, message);
                PlayerNetworkOperations.handleClientActionRequest(root, this);
            }

        } catch (IOException | SQLException e) {
            System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }
    }
}
