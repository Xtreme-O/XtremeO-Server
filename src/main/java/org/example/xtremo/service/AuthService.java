/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.service;

import java.time.LocalDateTime;
import org.example.xtremo.dao.PlayerDao;
import org.example.xtremo.mapper.PlayerMapper;
import org.example.xtremo.model.dto.PlayerDto;
import org.example.xtremo.model.entity.Player;
import utils.PasswordUtils;

/**
 *
 * @author hosam
 */
public class AuthService {
     private final PlayerDao playerDao;
    
    public AuthService(PlayerDao playerDao){
        this.playerDao = playerDao;
    }
    
    public PlayerDto login(String username, String password){
        
        Player player = playerDao.findByUsername(username).orElseThrow(()->new RuntimeException("Invalid username or password"));
        
        String incomingPasswordHash = PasswordUtils.hashPassword(password);
        
        if(!incomingPasswordHash.equals(player.getPasswordHash())){
            throw new RuntimeException("Invalid username or password");
        }
        
        player.setLastLogin(LocalDateTime.now());
        player.setStatus("online");
        playerDao.update(player);
        
        return PlayerMapper.toDto(player);
                
        
        
    }
}
