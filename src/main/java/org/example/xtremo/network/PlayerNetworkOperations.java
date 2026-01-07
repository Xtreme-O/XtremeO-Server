/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.example.xtremo.custom_exceptions.CustomException;
import org.example.xtremo.handlers.AuthenticationHandler;
import org.example.xtremo.model.dto.PlayerDTO;
import org.example.xtremo.network.protocol.Action;
import static org.example.xtremo.network.protocol.Action.LOGIN;
import static org.example.xtremo.network.protocol.Action.LOGOUT;
import static org.example.xtremo.network.protocol.Action.REGISTER;
import org.example.xtremo.network.protocol.ActionTypeMapper;
import org.example.xtremo.network.protocol.ProtocolMessageEnvelope;
import org.example.xtremo.network.protocol.RequestHeader;
import org.example.xtremo.network.protocol.models.InviteBody;
import org.example.xtremo.network.protocol.models.LoginBody;
import org.example.xtremo.network.protocol.models.LogoutBody;
import org.example.xtremo.network.protocol.models.RegisterBody;
import org.example.xtremo.session.SessionPlayer;
import org.example.xtremo.service.AuthService;
import org.example.xtremo.utils.DateTimeGsonAdapter;
import org.example.xtremo.utils.RequestHeaderAdapter;

/**
 *
 * @author wahid
 */
public class PlayerNetworkOperations {

    private PlayerNetworkOperations() {
        throw new IllegalAccessError();
    }

    private static Gson gsonConverter = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(RequestHeader.class, new RequestHeaderAdapter())
            .registerTypeAdapter(LocalDateTime.class, new DateTimeGsonAdapter())
            .create();

    public static Gson getGsonConverter() {
        return PlayerNetworkOperations.gsonConverter;
    }

    public static void sendResponse(ProtocolMessageEnvelope message, DataOutputStream out) throws IOException {
        String responseString = PlayerNetworkOperations.gsonConverter.toJson(message);
        int length = responseString.length();
        out.write(length);
        out.writeUTF(responseString);
    }

    public static void handleClientActionRequest(JsonObject rootJsonObject, PlayerConnectionHandler client) throws SQLException, Exception {
        AuthService authService = AuthService.getAuthService();
        String action = rootJsonObject
                .getAsJsonObject("header")
                .get("action")
                .getAsString();

        Action actionType = ActionTypeMapper.getActionType(action);
        switch (actionType) {
            case LOGIN -> {
                try {
                    ProtocolMessageEnvelope<LoginBody> req = gsonConverter.fromJson(rootJsonObject, new TypeToken<ProtocolMessageEnvelope<LoginBody>>() {
                    }.getType());
                    PlayerDTO pdto = AuthenticationHandler.handleLogin(authService, req);
                    ProtocolMessageEnvelope<PlayerDTO> response = new ProtocolMessageEnvelope<>(new RequestHeader("JSON", "RESPONSE"), pdto);
                    Server.activePlayers.put(pdto.id(), client);
                    PlayerNetworkOperations.sendResponse(response, client.getDos());

                } catch (Exception ex) {
                    System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }

            }
            case REGISTER -> {
                try {
                    ProtocolMessageEnvelope<RegisterBody> req = gsonConverter.fromJson(rootJsonObject, new TypeToken<ProtocolMessageEnvelope<RegisterBody>>() {
                    }.getType());
                    PlayerDTO pdto = AuthenticationHandler.handleRegister(authService, req);
                    ProtocolMessageEnvelope<PlayerDTO> response = new ProtocolMessageEnvelope<>(new RequestHeader("JSON", "RESPONSE"), pdto);
                    PlayerNetworkOperations.sendResponse(response, client.getDos());
                } catch (Exception ex) {
                    System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }

            }

            case LOGOUT -> {
                ProtocolMessageEnvelope<LogoutBody> req = gsonConverter.fromJson(rootJsonObject, new TypeToken<ProtocolMessageEnvelope<LogoutBody>>() {
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
            case INVITE -> {
                ProtocolMessageEnvelope<InviteBody> req = gsonConverter.fromJson(rootJsonObject, new TypeToken<ProtocolMessageEnvelope<InviteBody>>() {
                }.getType());
                System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) gsonConverter.toJson(req));
                
                PlayerDTO reciver = req.getBody().getPlayer2();
                PlayerDTO sender = req.getBody().getPlayer1();
                PlayerConnectionHandler reciverHandler = Server.activePlayers.getOrDefault(reciver.id(), null);
                if (reciverHandler == null) {
                    throw new CustomException.InviteError("User not active right now.");
                }
                try {
                    PlayerNetworkOperations.sendResponse(req, reciverHandler.getDos());
                } catch (IOException ex) {
                    System.getLogger(PlayerNetworkOperations.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }

            }
            case CONFIRM_INVITE -> {
                
                
                
                
            }

            default -> {
                throw new AssertionError();
            }

        }
    }

}
