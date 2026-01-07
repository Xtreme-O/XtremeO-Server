/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network.session;

import org.example.xtremo.network.PlayerConnectionHandler;

/**
 *
 * @author wahid
 */
public class SessionPlayer {
    private int id;
    private PlayerConnectionHandler handler;

    public SessionPlayer() {
    }

    public SessionPlayer(int id, PlayerConnectionHandler handler) {
        this.id = id;
        this.handler = handler;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlayerConnectionHandler getHandler() {
        return handler;
    }

    public void setHandler(PlayerConnectionHandler handler) {
        this.handler = handler;
    }
    
    
}
