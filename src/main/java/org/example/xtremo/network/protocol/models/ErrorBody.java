/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network.protocol.models;

/**
 *
 * @author wahid
 */
public class ErrorBody {
    String message;

    public ErrorBody() {
    }

    public ErrorBody(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
}
