/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.session;

import java.io.IOException;
import org.example.xtremo.network.PlayerNetworkOperations;
import org.example.xtremo.network.protocol.ProtocolMessageEnvelope;

/**
 *
 * @author wahid
 */
public class Session {

    private int id;
    private SessionPlayer sessionPlayer1;
    private SessionPlayer sessionPlayer2;

    public Session(int id, SessionPlayer sessionPlayer1, SessionPlayer sessionPlayer2) {
        this.id = id;
        this.sessionPlayer1 = sessionPlayer1;
        this.sessionPlayer2 = sessionPlayer2;
    }

    public void sendMessage(ProtocolMessageEnvelope message, int senderId) throws IOException {
        if (senderId == sessionPlayer1.getId()) {
            PlayerNetworkOperations.sendResponse(message, sessionPlayer2.getHandler().getDos());
        } else if (senderId == sessionPlayer2.getId()) {
            PlayerNetworkOperations.sendResponse(message, sessionPlayer1.getHandler().getDos());
        }
    }

    public Session() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SessionPlayer getSessionPlayer1() {
        return sessionPlayer1;
    }

    public void setSessionPlayer1(SessionPlayer sessionPlayer1) {
        this.sessionPlayer1 = sessionPlayer1;
    }

    public SessionPlayer getSessionPlayer2() {
        return sessionPlayer2;
    }

    public void setSessionPlayer2(SessionPlayer sessionPlayer2) {
        this.sessionPlayer2 = sessionPlayer2;
    }

}
