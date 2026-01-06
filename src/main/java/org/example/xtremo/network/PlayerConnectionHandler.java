package org.example.xtremo.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import org.example.xtremo.dao.PlayerDaoImpl;
import org.example.xtremo.database.DBConnection;
import org.example.xtremo.handlers.AuthenticationHandler;
import org.example.xtremo.model.dto.PlayerDTO;
import org.example.xtremo.network.protocol.Action;
import org.example.xtremo.network.protocol.ActionTypeMapper;
import org.example.xtremo.network.protocol.models.LoginBody;
import org.example.xtremo.network.protocol.models.RegisterBody;
import org.example.xtremo.network.protocol.ProtocolMessageEnvelope;
import org.example.xtremo.network.protocol.RequestHeader;
import org.example.xtremo.network.protocol.models.LogoutBody;
import org.example.xtremo.service.AuthService;
import org.example.xtremo.utils.RequestHeaderAdapter;

/**
 *
 * @author wahid
 */
public class PlayerConnectionHandler implements Runnable {

    private final Socket socket;

    public PlayerConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(socket.getInputStream()); DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

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

                Gson gson = PlayerNetworkOperations.getGsonConverter();

                JsonObject root = JsonParser.parseString(message).getAsJsonObject();
                String action = root
                        .getAsJsonObject("header")
                        .get("action")
                        .getAsString();

                System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, message);

                Action actionType = ActionTypeMapper.getActionType(action);
                AuthService authService = new AuthService();

                switch (actionType) {
                    case LOGIN -> {
                        try {
                            ProtocolMessageEnvelope<LoginBody> req = gson.fromJson(root, new TypeToken<ProtocolMessageEnvelope<LoginBody>>() {
                            }.getType());
                            PlayerDTO pdto = AuthenticationHandler.handleLogin(authService, req);
                            ProtocolMessageEnvelope<PlayerDTO> response = new ProtocolMessageEnvelope<>(new RequestHeader("JSON", "RESPONSE"), pdto);
                            PlayerNetworkOperations.sendResponse(response, dos);

                        } catch (Exception ex) {
                            System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                        }

                    }
                    case REGISTER -> {
                        try {
                            ProtocolMessageEnvelope<RegisterBody> req = gson.fromJson(root, new TypeToken<ProtocolMessageEnvelope<RegisterBody>>() {
                            }.getType());
                            PlayerDTO pdto = AuthenticationHandler.handleRegister(authService, req);
                            ProtocolMessageEnvelope<PlayerDTO> response = new ProtocolMessageEnvelope<>(new RequestHeader("JSON", "RESPONSE"), pdto);
                            PlayerNetworkOperations.sendResponse(response, dos);
                        } catch (Exception ex) {
                            System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                        }

                    }

                    case LOGOUT -> {
                        ProtocolMessageEnvelope<LogoutBody> req = gson.fromJson(root, new TypeToken<ProtocolMessageEnvelope<LogoutBody>>() {
                        }.getType());
                        boolean isUserLogedOut;
                        try {
                            isUserLogedOut = AuthenticationHandler.handleLogout(authService, req);

                            if (isUserLogedOut) {
                                System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, (String) "User loged out");
                            }
                        } catch (Exception ex) {
                            System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                        }

                    }

                    default -> {
                        throw new AssertionError();
                    }

                }

            }

        } catch (IOException | SQLException e) {
            System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }
    }
}
