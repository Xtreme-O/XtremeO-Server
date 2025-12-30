/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.handlers;

import org.example.xtremo.handlers.model.LoginCredintials;
import org.example.xtremo.handlers.model.RegisterCredintials;
import org.example.xtremo.model.dto.PlayerDTO;
import org.example.xtremo.service.AuthService;

/**
 *
 * @author wahid
 */
public class AuthenticationHandler {

    private AuthenticationHandler() {}
        
    
    public static PlayerDTO handleLogin(AuthService authService, LoginCredintials credintials) throws Exception{
        try {
            return authService.login(credintials.userName(), credintials.password());
            
        } catch (Exception e) {
            throw e;
        }
        
   
    }
    public static PlayerDTO handleRegister(AuthService authService, RegisterCredintials credintials) throws Exception{
        try {
            return authService.register(credintials.userName(), credintials.password(), credintials.avatar_url());
        } catch (Exception e) {
            throw e;
        }
        
    }
}