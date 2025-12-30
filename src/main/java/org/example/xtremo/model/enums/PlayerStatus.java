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
public enum PlayerStatus {
    ONLINE,
    OFFLINE,
    INGAME;
    
    public static PlayerStatus fromString(String value) {
        if (value == null) throw new IllegalArgumentException("Player Status cannot be null");
        return Arrays.stream(PlayerStatus.values())
                     .filter(e -> e.name().equalsIgnoreCase(value))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("Invalid GameType: " + value));
    }
    
}
