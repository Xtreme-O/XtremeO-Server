/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.model.enums;

import java.util.Arrays;

/**
 *
 * @author Abdelrahman
 */

public enum GameResult {
    IN_PROGRESS,
    WIN,
    DRAW;
    
    public static GameResult fromString(String value) {
        if (value == null) 
            throw new IllegalArgumentException("GameResult cannot be null");
        return Arrays.stream(GameResult.values())
                     .filter(e -> e.name().equalsIgnoreCase(value))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("Invalid GameResult: " + value));
    }
}