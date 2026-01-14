/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network.protocol.models;

/**
 *
 * @author wahid
 */
public class InviteConfirmedBody {
    int senderId;
    int receiverId;

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public InviteConfirmedBody() {
    }

    public InviteConfirmedBody(int senderId, int recieverId) {
        this.senderId = senderId;
        this.receiverId = recieverId;
    }
}
