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
public class GlobalMessageBody {
    private PlayerDTO sender;
    private String message;
    private String time;

    public GlobalMessageBody(PlayerDTO sender, String message, String time) {
        this.sender = sender;
        this.message = message;
        this.time = time;
    }

    public GlobalMessageBody() {
    }

    public PlayerDTO getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
    
    
    
}
