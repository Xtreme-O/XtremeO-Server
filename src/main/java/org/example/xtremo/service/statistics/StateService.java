package org.example.xtremo.service.statistics;

import org.example.xtremo.dao.PlayerDaoImpl;
import org.example.xtremo.model.enums.PlayerStatus;
import java.sql.SQLException;
import org.example.xtremo.dao.PlayerDaoAnalyticsExtended;
import org.example.xtremo.dao.PlayerDaoImplAnalyticsExtended;

public class StateService {
    private PlayerDaoAnalyticsExtended playerDao;

    public StateService() throws SQLException {
        
        this.playerDao = new PlayerDaoImplAnalyticsExtended();
    }
    
    public int getCount(PlayerStatus status) {
        return playerDao.countByStatus(status);
    }

//    public int getActiveConnectionsCount() {
//        return Server.players.size(); //
//    }

    public void listenToUpdates(org.example.xtremo.dao.OnDatabaseChangeListener listener) {
        PlayerDaoImpl.setOnDatabaseChangeListener(listener);
    }
}