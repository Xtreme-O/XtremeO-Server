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
            case "MOVE"                 -> Action.MOVE;
            case "WIN"                  -> Action.WIN;
            case "DRAW"                 -> Action.DRAW;
            case "ERROR"                -> Action.ERROR;
            case "LOGOUT"               -> Action.LOGOUT;
            case "LOBBY"                ->  Action.LOBBY;
            case "UNKNOWN"              -> Action.UNKNOWN;
            case "INVITE"               -> Action.INVITE;
            case "CONFIRM_INVITE"       -> Action.INVITE_CONFIRMED;
            case "SESSION_ENDED"        -> Action.SESSION_ENDED;
            case "PARTNER_DISCONNECTED" -> Action.PARTNER_DISCONNECTED;
            case "SESSION_MESSAGE"      -> Action.SESSION_MESSAGE;
            case "GET_ACTIVE_USERS"     -> Action.GET_ACTIVE_USERS;
            case "GLOBAL_MESSAGE"       -> Action.GLOBAL_MESSAGE;
            case "IN_GAME_MESSAGE"      -> Action.IN_GAME_MESSAGE;
            default                     -> Action.UNKNOWN;
        };



    }

}