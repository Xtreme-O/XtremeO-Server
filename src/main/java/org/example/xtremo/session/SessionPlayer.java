/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.session;

import java.util.Objects;
import org.example.xtremo.network.PlayerConnectionHandler;

/**
 *
 * @author wahid
 */
public class SessionPlayer {

    private final int playerId;
    private volatile PlayerConnectionHandler handler;

    public SessionPlayer(int playerId, PlayerConnectionHandler handler) {
        this.playerId = playerId;
        this.handler = handler;
    }

    public int getPlayerId() {
        return playerId;
    }

    public PlayerConnectionHandler getHandler() {
        return handler;
    }

    public void setHandler(PlayerConnectionHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionPlayer)) return false;
        SessionPlayer that = (SessionPlayer) o;
        return playerId == that.playerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId);
    }
}