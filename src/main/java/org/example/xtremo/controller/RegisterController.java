/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.controller;

import org.example.xtremo.model.dto.PlayerDto;
import org.example.xtremo.service.AuthService;

/**
 *
 * @author monaz
 */
public class RegisterController {

    private final AuthService authService;

    public RegisterController(AuthService authService) {
        this.authService = authService;
    }

    public PlayerDto register(String username, String password, String avatarUrl) {
        return authService.register(username, password, avatarUrl);
    }
}
