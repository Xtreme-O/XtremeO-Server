/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Abdelrahman
 */
package org.example.xtremo.model.enums;

import java.util.Arrays;

public enum GameType {
    TIC_TAC_TOE;
    
    public static GameType fromString(String value) {
        if (value == null) throw new IllegalArgumentException("GameType cannot be null");
        return Arrays.stream(GameType.values())
                     .filter(e -> e.name().equalsIgnoreCase(value))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("Invalid GameType: " + value));
    }
   
}
