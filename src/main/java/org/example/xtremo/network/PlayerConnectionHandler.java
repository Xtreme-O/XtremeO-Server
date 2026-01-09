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
import org.example.xtremo.custom_exceptions.CustomException;
import org.example.xtremo.network.protocol.Action;
import org.example.xtremo.network.protocol.ProtocolMessageEnvelope;
import org.example.xtremo.network.protocol.RequestHeader;
import org.example.xtremo.network.protocol.models.ErrorBody;

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
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, "PlayerConnectionHandler " + ex);
        }
    }

    @Override
    public void run() {
        try {
            socket.setTcpNoDelay(true);
            socket.setKeepAlive(true);
            socket.setSoTimeout(0);

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
        } catch (CustomException.InviteError inviteError) {
            try {
                System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) "User not active right now.");

                RequestHeader header = new RequestHeader("JSON", Action.INVITE_CONFIRMED.name());
                ErrorBody errorBody = new ErrorBody(inviteError.getMessage());
                ProtocolMessageEnvelope<ErrorBody> message = new ProtocolMessageEnvelope<ErrorBody>(header, errorBody);
                PlayerNetworkOperations.sendResponse(message, dos);
                
            } catch (IOException ex1) {
                System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex1);
            }
        } catch (Exception ex) {
            System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}