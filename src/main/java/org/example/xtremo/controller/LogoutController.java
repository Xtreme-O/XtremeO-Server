package org.example.xtremo.controller;


import org.example.xtremo.service.AuthService;



public class LogoutController {
    private  final AuthService authService;

    public LogoutController(AuthService authService) {
        this.authService = authService;
    }
    public void logout(int playerId){
        authService.logout(playerId);
    }

}

