/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network.protocol.models;

/**
 *
 * @author wahid
 */
public class InviteDeclinedBody {
    private int senderId;
    private int recievrId;

    public InviteDeclinedBody(int senderId, int recievrId) {
        this.senderId = senderId;
        this.recievrId = recievrId;
    }

    public InviteDeclinedBody() {
    }

    public int getSenderId() {
        return senderId;
    }

    public int getRecievrId() {
        return recievrId;
    }
    
        
}
