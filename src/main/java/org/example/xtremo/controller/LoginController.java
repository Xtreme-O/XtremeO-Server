package org.example.xtremo.controller;

import org.example.xtremo.model.dto.PlayerDTO;
import org.example.xtremo.service.AuthService;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hosam
 */
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    public PlayerDTO login(String username, String password) {
        return authService.login(username, password);
    }
}
