/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.custom_exceptions;

/**
 *
 * @author wahid
 */
public abstract class CustomException{
    public static class InviteError extends Exception{
        public InviteError(String message) {
            super(message);
        }
        
    }
    public static class SocketError extends Exception{
        public SocketError(String message) {
            super(message);
        }
    }
    public static class ConnectionLost extends Exception{
        
        public ConnectionLost(String message) {
            super(message);
        }
    }
    public static class InviteDeclined extends Exception{
        
        public InviteDeclined(String message) {
            super(message);
        }
    }
    
    
}
