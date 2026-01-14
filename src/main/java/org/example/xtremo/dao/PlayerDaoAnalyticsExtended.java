/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.dao;

import java.sql.SQLException;
import org.example.xtremo.model.enums.PlayerStatus;

/**
 *
 * @author wahid
 */
public abstract class PlayerDaoAnalyticsExtended extends PlayerDaoImpl{
    
    public PlayerDaoAnalyticsExtended() throws SQLException{
        super();
    }
    public abstract int countByStatus(PlayerStatus status); // mona
}
