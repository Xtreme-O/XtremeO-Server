package org.example.xtremo.network;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import org.example.xtremo.dao.PlayerDaoImpl;
import org.example.xtremo.database.DBConnection;
import org.example.xtremo.model.dto.PlayerDTO;
import org.example.xtremo.network.protocol.Action;
import org.example.xtremo.network.protocol.ActionTypeMapper;
import org.example.xtremo.network.protocol.MessageType;
import org.example.xtremo.network.protocol.MessageTypeMapper;
import org.example.xtremo.service.AuthService;

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
                JsonObject obj = JsonParser.parseString(message).getAsJsonObject();
                
                
                JsonObject header = obj.get("header").getAsJsonObject();
                JsonObject data = obj.get("data").getAsJsonObject();
                
                
                String action = header.get("action").getAsString();
                String type = header.get("type").getAsString();
                
                
                System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, message);
                
                System.out.println(data.get("username").getAsString());
                System.out.println(data.get("password").getAsString());
                
                
                Action actionType = ActionTypeMapper.getActionType(action);
                MessageType messageType = MessageTypeMapper.getMessageType(type);
                
                
                switch (messageType) {
                    case ERROR      -> {break;}
                    case EVENT      -> {break;}
                    case REQUEST    -> {break;}
                    case RESPONSE   -> {
                        
                        
                        switch (actionType) {
                            case REGISTER -> {
                                System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, "In REGISTER cluse");
                                    AuthService authService;
                                    try {
                                        authService = new AuthService(new PlayerDaoImpl(DBConnection.getConnection()));
                                        String username = data.get("username").getAsString(); 
                                        String password = data.get("password").getAsString();

                                        System.out.println(username);
                                        System.out.println(password);


                                        System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, () -> username + password);
                                        PlayerDTO player = authService.register(username, password,"url");
                                        
                                        System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, "User has registered"+player.username());
                                    } catch (SQLException | RuntimeException ex) {
                                        System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                                    }

                                    break;
                            
                            }
                            case LOGIN -> {
//                                System.out.println(data.getAsString());
                                System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, "In LOGIN cluse");
                                AuthService authService;
                                try {
                                    authService = new AuthService(new PlayerDaoImpl(DBConnection.getConnection()));
                                    String username = data.get("username").getAsString(); 
                                    String password = data.get("password").getAsString();
                                    
                                    System.out.println(username);
                                    System.out.println(password);
                                    
                                    
                                    System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, () -> username + password);
                                    PlayerDTO player = authService.login(username, password);
                                    System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.INFO, "User has logged in{0}", player.username());
                                } catch (SQLException ex) {
                                    System.getLogger(PlayerConnectionHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                                }
                                
                                break;
                                
                                
                            }


                            case DRAW       -> {}
                            case ERROR      -> {}
                            case EXIT       -> {}
                            case MOVE       -> {}
                            case START      -> {}
                            case TURN       -> {}
                            case UNKNOWN    -> {}
                            case WAITING    -> {}
                            case WIN        -> {}
                            default         -> throw new AssertionError();
                        }
                    }
                    case UNKNOWN -> {
                    }
                    default -> throw new AssertionError();
                }
                
                
                
                
                
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
        }
    }
}

//} "{"header": { "action": "LOGIN", "type":"Response" },"data": { "username": "username","password":"user hashed password" }}";