/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network;

import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import org.example.xtremo.network.protocol.Action;
import org.example.xtremo.network.protocol.DataType;
import org.example.xtremo.network.protocol.Header;
import org.example.xtremo.network.protocol.Message;
import org.example.xtremo.network.protocol.MessageType;
import org.example.xtremo.network.protocol.Move;
import org.example.xtremo.service.game.Match;
import org.example.xtremo.service.game.MatchManager;

/**
 *
 * @author Elsobky
 * 
 */

public class ClientHandler implements Runnable {

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private final Gson gson = new Gson();
    private Match match;
    private char symbol;
    private boolean connected = true;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            initStreams();
            MatchManager.addPlayer(this);
            
            while (connected) {
                String msg = dataInputStream.readUTF();
                handleMessage(msg);
            }


        } catch (IOException e) {
            System.out.println("Client disconnected");
        } finally {
            cleanup();
        }
    }

    private void initStreams() throws IOException {
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }    
    
    private void handleMessage(String json) {
        try {
            Message message = gson.fromJson(json, Message.class);
            Action action = message.getHeader().getAction();

            switch (action) {
                case MOVE -> handleMove(message);
                case EXIT -> cleanup();
                default -> send(new Message(
                        new Header(Action.ERROR, MessageType.ERROR),
                        Map.of(DataType.MESSEGE, "UNKNOWN_ACTION"))
                );
                
            }

        } catch (Exception e) {
            send(new Message(
                    new Header(Action.ERROR, MessageType.ERROR),
                    Map.of(DataType.MESSEGE, "BAD_JSON"))
            );
        }
    }
    
    private void handleMove(Message message) {
        Move move = gson.fromJson(
                gson.toJson(message.getData()),
                Move.class
        );
        match.handleMove(this, move.getRow(), move.getCol());
    }

    public synchronized void send(Message message) {
        if (!connected) return;
        try {
            dataOutputStream.writeUTF(gson.toJson(message));
            dataOutputStream.flush();
        } catch (IOException e) {
            cleanup();
        }
    }

    private synchronized void cleanup() {
        if (!connected) return;
        connected = false;

        try {
            MatchManager.removePlayer(this);

            if (dataInputStream != null) dataInputStream.close();
            if (dataOutputStream != null) dataOutputStream.close();
            if (socket != null && !socket.isClosed()) socket.close();

        } catch (IOException e) {
            
        }
    }


    public void setMatch(Match match) {
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}

