/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.network.protocol.models;

import java.util.Arrays;

/**
 *
 * @author wahid
 */
public enum GameState {
    IN_PROGRESS,
    WIN,
    DRAW;
    
    public static GameState fromString(String value) {
        if (value == null) 
            throw new IllegalArgumentException("GameState cannot be null");
        return Arrays.stream(GameState.values())
                     .filter(e -> e.name().equalsIgnoreCase(value))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("Invalid GameState: " + value));
    }
}
