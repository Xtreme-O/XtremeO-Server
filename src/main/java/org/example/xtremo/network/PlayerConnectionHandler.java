package org.example.xtremo.network;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author wahid
 */
public class PlayerConnectionHandler implements Runnable {

    private final Socket socket;
    DataOutputStream dos;
    DataInputStream dis;
    public PlayerConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            while (true) {
                String message = dis.readUTF();
                JsonObject obj = JsonParser.parseString(message).getAsJsonObject();
                
                
                JsonObject header = obj.get("header").getAsJsonObject();
                JsonObject data = obj.get("data").getAsJsonObject();
                String action = header.get("action").getAsString();
                
                
                switch (action) {
                    case "":
                        break;
                    default:
                        throw new AssertionError();
                }
                
                
                
                
                
//                String type = header.get("type").getAsString();
//                String username = data.get("username").getAsString();
//                String password = data.get("password").getAsString();
//                System.out.println("action = " + action);
//                System.out.println("type = " + type);
//                System.out.println("username = " + username);
//                System.out.println("password = " + password);
//                System.out.println("Received: " + obj.getAsString());

                dos.flush();
            }
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}