/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.handlers;


import org.example.xtremo.model.dto.PlayerDTO;
import org.example.xtremo.network.protocol.LoginBody;
import org.example.xtremo.network.protocol.RegisterBody;
import org.example.xtremo.network.protocol.RequestEnvelope;
import org.example.xtremo.service.AuthService;

/**
 *
 * @author wahid
 */
public class AuthenticationHandler {

    private AuthenticationHandler() {}
    
    public static PlayerDTO handleLogin(AuthService authService, RequestEnvelope<LoginBody> request) throws Exception{
        try {
            
            LoginBody body = request.getBody();
            
            return authService.login(body.getUsername(),body.getPassword());
            
        } catch (Exception e) {
            throw e;
        }
        
   
    }
    public static PlayerDTO handleRegister(AuthService authService, RequestEnvelope<RegisterBody> request) throws Exception{
        try {
            RegisterBody body = request.getBody();
            return authService.register(body.getUsername(), body.getPassword(), body.getAvtar_url());
        } catch (Exception e) {
            throw e;
        }
        
    }
}