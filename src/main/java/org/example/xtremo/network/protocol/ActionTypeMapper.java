/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network.protocol;

/**
 *
 * @author wahid
 */
public class ActionTypeMapper {
    private ActionTypeMapper(){}
    
    public static Action getActionType(String message){
        
       return switch (message.toUpperCase()) {
            case "LOGIN"                -> Action.LOGIN;
            case "REGISTER"             -> Action.REGISTER;
            case "START"                -> Action.START;
            case "WAITING"              -> Action.WAITING;
            case "TURN"                 -> Action.TURN;
            case "MOVE"                 -> Action.MOVE;
            case "WIN"                  -> Action.WIN;
            case "DRAW"                 -> Action.DRAW;
            case "EXIT"                 -> Action.EXIT;
            case "ERROR"                -> Action.ERROR;
            case "LOGOUT"               -> Action.LOGOUT;
            case "UNKNOWN"              -> Action.UNKNOWN;
            case "INVITE"               -> Action.INVITE;
            case "CONFIRM_INVITE"       -> Action.INVITE_CONFIRMED;
            case "INVITE_DECLIENED"     -> Action.INVITE_DECLIENED;
            case "SESSION_ENDED"        -> Action.SESSION_ENDED;
            case "PARTNER_DISCONNECTED" -> Action.PARTNER_DISCONNECTED;
            case "SESSION_MESSAGE"      -> Action.SESSION_MESSAGE;
            default                     -> Action.UNKNOWN;
        };
        
        
        
    }
    
}
