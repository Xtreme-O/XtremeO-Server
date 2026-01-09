/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.session;
import java.util.concurrent.ConcurrentHashMap;
/**
 *
 * @author wahid
 */
public class SessionManager {

    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, String> playerToSession = new ConcurrentHashMap<>();

    public void register(Session session) {
        sessions.put(session.getId(), session);
        playerToSession.put(session.getPlayer1().getPlayerId(), session.getId());
        playerToSession.put(session.getPlayer2().getPlayerId(), session.getId());
    }

    public Session getByPlayer(int playerId) {
        String sessionId = playerToSession.get(playerId);
        return sessionId == null ? null : sessions.get(sessionId);
    }

    public void remove(Session session) {
        sessions.remove(session.getId());
        playerToSession.remove(session.getPlayer1().getPlayerId());
        playerToSession.remove(session.getPlayer2().getPlayerId());
    }
}