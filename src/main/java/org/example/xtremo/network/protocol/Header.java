/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network.protocol;

/**
 *
 * @author Elsobky
 */

public class Header {
    private Action action;
    private MessageType type;

    public Header(Action action, MessageType type) {
        this.action = action;
        this.type = type;
    }

    public Action getAction() {
        return action;
    }

    public MessageType getType() {
        return type;
    }
}

