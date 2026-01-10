/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network.protocol.models;

/**
 *
 * @author wahid
 */
public class SessionMessageBody {
    
    private Move move;
    private GameState state;

    public GameState getState() {
        return state;
    }

    public SessionMessageBody(Move move, GameState state) {
        this.move = move;
        this.state = state;
    }
    
    public Move getMove() {
        return move;
    }

    public SessionMessageBody() {
    }
    
    
}