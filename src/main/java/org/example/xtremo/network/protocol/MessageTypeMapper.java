/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network.protocol;

/**
 *
 * @author wahid
 */
public class MessageTypeMapper {
    private MessageTypeMapper(){}
    
    public static MessageType getMessageType(String message){
        
       return switch (message.toUpperCase()) {
            case "REQUEST"  -> MessageType.REQUEST;
            case "RESPONSE" -> MessageType.RESPONSE;
            case "EVENT"    -> MessageType.EVENT;
            case "ERROR"    -> MessageType.ERROR;
            default         -> MessageType.UNKNOWN;
        };
    }
}