/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.handlers;

import org.example.xtremo.model.dto.PlayerDTO;
import org.example.xtremo.network.protocol.models.LoginBody;
import org.example.xtremo.network.protocol.models.RegisterBody;
import org.example.xtremo.network.protocol.ProtocolMessageEnvelope;
import org.example.xtremo.network.protocol.models.LogoutBody;
import org.example.xtremo.service.AuthService;

/**
 *
 * @author wahid
 */
public class AuthenticationHandler {

    private AuthenticationHandler() {
        throw new IllegalAccessError();
    }

    public static PlayerDTO handleLogin(AuthService authService, ProtocolMessageEnvelope<LoginBody> request) throws Exception {
        try {

            LoginBody body = request.getBody();

            return authService.login(body.getUsername(), body.getPassword());

        } catch (Exception e) {
            throw e;
        }

    }

    public static PlayerDTO handleRegister(AuthService authService, ProtocolMessageEnvelope<RegisterBody> request) throws Exception {
        try {
            RegisterBody body = request.getBody();
            return authService.register(body.getUsername(), body.getPassword(), body.getAvatarUrl());
        } catch (Exception e) {
            throw e;
        }

    }

    public static boolean handleLogout(AuthService authService, ProtocolMessageEnvelope<LogoutBody> request) throws Exception {
        try {
            LogoutBody body = request.getBody();
            return authService.logout(body.username);
        } catch (Exception e) {
            throw e;
        }

    }

}
