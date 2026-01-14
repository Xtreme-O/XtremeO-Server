/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network.protocol.models;

/**
 *
 * @author Elsobky
 */
public class Move {
    private int row;
    private int col;
    private MovePlayer player;

    public MovePlayer getPlayer() {
        return player;
    }

    public Move(int row, int col, MovePlayer player) {
        this.row = row;
        this.col = col;
        this.player = player;
    }

    public Move() {
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
