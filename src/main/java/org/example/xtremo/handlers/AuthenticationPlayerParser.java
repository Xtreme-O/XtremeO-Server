/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.handlers;

import com.google.gson.JsonObject;
import org.example.xtremo.handlers.model.LoginCredintials;
import org.example.xtremo.handlers.model.RegisterCredintials;

/**
 *
 * @author wahid
 */
public class AuthenticationPlayerParser {
    
    public static String USER_PASSWORD = "password";
    public static String USER_NAME = "username";
    public static String USER_AVATAR_URL = "avatar_url";
    
    
    
    
    private AuthenticationPlayerParser() {}
    
    
    
    public static LoginCredintials parseFromJasonToPlayerLoginCredintials(JsonObject jsonObject){     
        JsonObject data = jsonObject.get("data").getAsJsonObject();
        String username = data.get(USER_NAME).getAsString();
        String password = data.get(USER_PASSWORD).getAsString();
        return new LoginCredintials(username, password);
    }
    
    
    public static RegisterCredintials parseFromJasonToPlayerRegisterCredintials(JsonObject jsonObject){
        JsonObject data = jsonObject.get("data").getAsJsonObject();
        String username = data.get(USER_NAME).getAsString();
        String password = data.get(USER_PASSWORD).getAsString();
        String avatarUrl = data.get(USER_AVATAR_URL).getAsString();
        return new RegisterCredintials(username, password, avatarUrl);    
    }
    
}
