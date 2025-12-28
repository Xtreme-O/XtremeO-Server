/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.service.game;

import java.util.LinkedList;
import java.util.Vector;
import org.example.xtremo.network.ClientHandler;
import org.example.xtremo.network.protocol.Action;
import org.example.xtremo.network.protocol.Header;
import org.example.xtremo.network.protocol.Message;
import org.example.xtremo.network.protocol.MessageType;

/**
 *
 * @author Elsobky
 */

public class MatchManager {

    private static final Vector<ClientHandler> waitingPlayers = new Vector<>();

    private MatchManager() { }

    public static void addPlayer(ClientHandler player) {
        waitingPlayers.add(player);
        player.send(new Message(
                new Header(Action.WAITING, MessageType.EVENT),
                null
        ));

        while (waitingPlayers.size() >= 2) {
            ClientHandler p1 = waitingPlayers.remove(0);
            ClientHandler p2 = waitingPlayers.remove(0);
            Match match = new Match(p1, p2);
            match.start();
        }
    }

    public static void removePlayer(ClientHandler player) {
        waitingPlayers.remove(player);
    }
}

