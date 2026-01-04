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
import org.example.xtremo.handlers.AuthenticationPlayerParser;
import org.example.xtremo.handlers.model.LoginCredintials;
import org.example.xtremo.handlers.model.RegisterCredintials;
import org.example.xtremo.model.dto.PlayerDTO;
import org.example.xtremo.network.protocol.Action;
import static org.example.xtremo.network.protocol.Action.EXIT;
import static org.example.xtremo.network.protocol.Action.MOVE;
import static org.example.xtremo.network.protocol.Action.START;
import static org.example.xtremo.network.protocol.Action.TURN;
import static org.example.xtremo.network.protocol.Action.UNKNOWN;
import static org.example.xtremo.network.protocol.Action.WAITING;
import static org.example.xtremo.network.protocol.Action.WIN;
import org.example.xtremo.network.protocol.ActionTypeMapper;
import org.example.xtremo.network.protocol.LoginBody;
import org.example.xtremo.network.protocol.MessageType;
import org.example.xtremo.network.protocol.MessageTypeMapper;
import org.example.xtremo.network.protocol.RegisterBody;
import org.example.xtremo.network.protocol.RequestEnvelope;
import org.example.xtremo.network.protocol.RequestHeader;
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
        try (DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
            
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
                
                Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(RequestHeader.class,
                        new RequestHeaderAdapter())
                .create();
                
                JsonObject root = JsonParser.parseString(message).getAsJsonObject();
                
                
                JsonObject headers = root.get("header").getAsJsonObject();
                JsonObject headerObj = root.getAsJsonObject("header");
                String action = headerObj.get("action").getAsString();
                
                
                System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, message);
                        
                Action actionType = ActionTypeMapper.getActionType(action);
                AuthService authService = new AuthService(new PlayerDaoImpl(DBConnection.getConnection()));
                
                switch (actionType) {
                    case LOGIN ->{
                        
                      
                        try {
                            RequestEnvelope<LoginBody> req =
                            gson.fromJson(root,
                            new TypeToken<RequestEnvelope<LoginBody>>(){}.getType());
                            PlayerDTO pdto = AuthenticationHandler.handleLogin(authService, req);
                            System.out.println(pdto);
                            
                        } catch (Exception ex) {
                            System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                        }
                        
                    }
                    case REGISTER->{
                        try {
                            RequestEnvelope<RegisterBody> req =
                            gson.fromJson(root,
                                new TypeToken<RequestEnvelope<RegisterBody>>(){}.getType());
                            PlayerDTO pdto = AuthenticationHandler.handleRegister(authService, req);
                            System.out.println(pdto);
                            
                        } catch (Exception ex) {
                            System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                        }
                        
                        
                        
                    }
                    default->{
                        throw new AssertionError();
                    }
                        
                }
                
                
//                    case RESPONSE   -> {
//                        switch (actionType) {
//                            case REGISTER -> {
//                                System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, "In REGISTER cluse");
//                                    try {
//                                        RegisterCredintials credintials = AuthenticationPlayerParser.parseFromJasonToPlayerRegisterCredintials(obj);
//                                        System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, () -> credintials.userName() +" "+ credintials.password());
//                                        PlayerDTO player = AuthenticationHandler.handleRegister(authService, credintials);
//                                        System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, "User has registered{0}", player.username());
//                                    } catch (SQLException | RuntimeException ex) {
//                                        System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
//                                    } catch (Exception ex) {
//                                        System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
//                                    }
//                                    break;
//                            }
//                            case LOGIN -> {
//                                System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, "In LOGIN cluse");
//                                try {
//                                    LoginCredintials credintials = AuthenticationPlayerParser.parseFromJasonToPlayerLoginCredintials(obj);
//                                    System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, () -> credintials.userName() + credintials.password());
//                                    PlayerDTO player = AuthenticationHandler.handleLogin(authService, credintials);
//                                    System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, "User has logged in{0}", player.username());
//                                } catch (SQLException ex) {
//                                    System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
//                                } catch (Exception ex) {
//                                    System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
//                                }
//                                break;
//                            }
//                            default -> throw new AssertionError();
//                        }
//                    }
//               
                
                
                
                
                
//                String type = header.get("type").getAsString();
//                String username = data.get("username").getAsString();
//                String password = data.get("password").getAsString();
//                System.out.println("action = " + action);
//                System.out.println("type = " + type);
//                System.out.println("username = " + username);
//                System.out.println("password = " + password);
//                System.out.println("Received: " + obj.getAsString());

                
            }
            

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}

//} "{"header": { "action": "LOGIN", "type":"Response" },"data": { "username": "username","password":"user hashed password" }}";