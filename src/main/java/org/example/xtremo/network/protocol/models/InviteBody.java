/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network.protocol.models;

import org.example.xtremo.model.dto.PlayerDTO;

/**
 *
 * @author wahid
 */
public class InviteBody {
    PlayerDTO player1;
    PlayerDTO player2;

    public PlayerDTO getPlayer1() {
        return player1;
    }

    public PlayerDTO getPlayer2() {
        return player2;
    }

    public InviteBody(PlayerDTO player1, PlayerDTO player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public InviteBody() {
    }
    
    
}
