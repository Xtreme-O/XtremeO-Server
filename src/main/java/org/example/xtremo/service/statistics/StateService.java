package org.example.xtremo.service.statistics;

import org.example.xtremo.dao.PlayerDao;
import org.example.xtremo.dao.PlayerDaoImpl;
import org.example.xtremo.model.enums.PlayerStatus;
import java.sql.SQLException;

public class StateService {
    private PlayerDao playerDao;

    public StateService() {
        try {
            this.playerDao = new PlayerDaoImpl(); //
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCount(PlayerStatus status) {
        return playerDao.countByStatus(status); //
    }

//    public int getActiveConnectionsCount() {
//        return Server.players.size(); //
//    }

    public void listenToUpdates(org.example.xtremo.dao.OnDatabaseChangeListener listener) {
        PlayerDaoImpl.setOnDatabaseChangeListener(listener); //
    }
}