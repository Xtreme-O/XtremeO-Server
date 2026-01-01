/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.service;

import java.time.LocalDateTime;
import org.example.xtremo.dao.PlayerDao;
import org.example.xtremo.mapper.PlayerMapper;
import org.example.xtremo.model.dto.PlayerDTO;
import org.example.xtremo.model.entity.Player;
import org.example.xtremo.model.enums.PlayerStatus;
import org.example.xtremo.utils.PasswordUtils;

/**
 *
 * @author monaz
 */
public class AuthService {
     private final PlayerDao playerDao;
    
    public AuthService(PlayerDao playerDao){
        this.playerDao = playerDao;
    }
    
    public PlayerDTO login(String username, String password){
        
        Player player = playerDao.findByUsername(username).orElseThrow(()->new RuntimeException("Invalid username or password"));
        
        String incomingPasswordHash = PasswordUtils.hashPassword(password);
        
        if(!incomingPasswordHash.equals(player.getPasswordHash())){
            throw new RuntimeException("Invalid username or password");
        }
        
        player.setLastLogin(LocalDateTime.now());
        player.setStatus(
                PlayerStatus.ONLINE
        );
        playerDao.update(player);
        
        return PlayerMapper.toDto(player);
                
        
        
    }
    
    public PlayerDTO register(String username, String password, String avatarUrl)throws RuntimeException{
    System.out.println("In register");
    if (playerDao.findByUsername(username).isPresent()) {
        System.out.println("Username already exists");
        throw new RuntimeException("Username already exists");
    }

    String passwordHash = PasswordUtils.hashPassword(password);

    Player player = new Player(
            username,
            passwordHash,
            avatarUrl,
            PlayerStatus.OFFLINE
    );

    System.out.println("before save");
    Player savedPlayer = playerDao.save(player);
    System.out.println("After save");
    return PlayerMapper.toDto(savedPlayer);
}

public  void logout(int playerId){
        Player player = playerDao.findById(playerId).orElseThrow(()->new RuntimeException("Player not found"));
        player.setStatus(PlayerStatus.OFFLINE);
        playerDao.update(player);
}
}
