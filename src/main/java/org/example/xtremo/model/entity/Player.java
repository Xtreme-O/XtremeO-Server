package org.example.xtremo.model.entity;

import java.time.LocalDateTime;

public class Player {

    private int id;
    private String username;
    private String passwordHash;
    private String avatarUrl;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public Player() {
    }

    public Player(String username, String passwordHash, String avatarUrl, String status) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.avatarUrl = avatarUrl;
        this.status = status;
    }
    
    public Player(int id, String username, String passwordHash, String avatarUrl, String status, LocalDateTime createdAt, LocalDateTime lastLogin) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.avatarUrl = avatarUrl;
        this.status = status;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }



    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
