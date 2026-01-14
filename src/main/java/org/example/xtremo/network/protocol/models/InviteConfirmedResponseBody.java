/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network.protocol.models;

/**
 *
 * @author wahid
 */
public class InviteConfirmedResponseBody {
    
    private MovePlayer player1;
    private MovePlayer player2;

    public InviteConfirmedResponseBody() {
    }

    public InviteConfirmedResponseBody(MovePlayer player1, MovePlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public MovePlayer getPlayer1() {
        return player1;
    }

    public void setPlayer1(MovePlayer player1) {
        this.player1 = player1;
    }

    public MovePlayer getPlayer2() {
        return player2;
    }

    public void setPlayer2(MovePlayer player2) {
        this.player2 = player2;
    }
    
    
}
