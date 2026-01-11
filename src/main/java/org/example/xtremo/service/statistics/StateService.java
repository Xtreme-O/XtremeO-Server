package org.example.xtremo.service.statistics;

import org.example.xtremo.dao.PlayerDaoImpl;
import org.example.xtremo.model.enums.PlayerStatus;
import java.sql.SQLException;
import org.example.xtremo.dao.PlayerDaoAnalyticsExtended;
import org.example.xtremo.dao.PlayerDaoImplAnalyticsExtended;
import org.example.xtremo.session.SessionManager;

public class StateService {
    private PlayerDaoAnalyticsExtended playerDao;
    private SessionManager sessionManager;

    public StateService() throws SQLException {
        this.sessionManager = SessionManager.getManager();
        this.playerDao = new PlayerDaoImplAnalyticsExtended();
    }
    
    public int getCount(PlayerStatus status) {
        return playerDao.countByStatus(status);
    }

    public int getActiveMatchesCount() {
        return sessionManager.getSessionsCount();
    }
//    public int getActiveConnectionsCount() {
//        return Server.players.size(); //
//    }

    public void listenToUpdates(org.example.xtremo.dao.OnDatabaseChangeListener listener) {
        PlayerDaoImpl.setOnDatabaseChangeListener(listener);
    }
}