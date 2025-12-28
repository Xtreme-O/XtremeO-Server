/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.example.xtremo.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import org.example.xtremo.dao.GameDao;
import org.example.xtremo.dao.GameDaoImpl;
import org.example.xtremo.dao.GameMoveDao;
import org.example.xtremo.dao.GameMoveDaoImpl;
import org.example.xtremo.dao.PlayerDao;
import org.example.xtremo.dao.PlayerDaoImpl;
import org.example.xtremo.dao.PlayerScoreDao;
import org.example.xtremo.dao.PlayerScoreDaoImpl;
import org.example.xtremo.database.DBConnection;


/**
 * FXML Controller class
 *
 * @author Abdelrahman
 */
public class PrimaryController implements Initializable {


    @FXML
    private Button primaryButton;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void switchToSecondary(ActionEvent event) throws SQLException {

        PlayerDao playerDao = new PlayerDaoImpl(DBConnection.getConnection());
        GameDao gameDao = new GameDaoImpl(DBConnection.getConnection());
        GameMoveDao gameMoveDao = new GameMoveDaoImpl(DBConnection.getConnection());
        PlayerScoreDao scoreDao = new PlayerScoreDaoImpl(DBConnection.getConnection());
        
         System.out.println("=== PLAYERS ===");
        playerDao.findAll()
                .forEach(p -> System.out.println(p.getUsername()));

        System.out.println("=== GAMES ===");
        gameDao.findAll()
                .forEach(g -> System.out.println(g.getGameType()));

        System.out.println("=== MOVES ===");
        gameMoveDao.findAll()
                .forEach(m -> System.out.println(m.getMoveNumber()));

        System.out.println("=== SCORES ===");
        scoreDao.findAll()
                .forEach(s -> System.out.println(s.getWins()));
        

    }

}
