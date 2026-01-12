/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network.protocol.models;

/**
 *
 * @author wahid
 */
public class MovePlayer {
    private String name;
    private String symobl;

    public MovePlayer() {
    }

    public String getName() {
        return name;
    }

    public String getSymobl() {
        return symobl;
    }

    public MovePlayer(String name, String symobl) {
        this.name = name;
        this.symobl = symobl;
    }
    
    
}
