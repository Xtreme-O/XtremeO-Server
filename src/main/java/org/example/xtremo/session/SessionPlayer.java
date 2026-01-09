/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.session;

import org.example.xtremo.network.PlayerConnectionHandler;

/**
 *
 * @author wahid
 */
public class SessionPlayer {
    private PlayerConnectionHandler handler;

    public SessionPlayer() {
    }

    public SessionPlayer(PlayerConnectionHandler handler) {

        this.handler = handler;
    }
    public PlayerConnectionHandler getHandler() {
        return handler;
    }

    public void setHandler(PlayerConnectionHandler handler) {
        this.handler = handler;
    }
    
    
}
