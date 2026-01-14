/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.session;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
/**
 *
 * @author wahid
 */
public class SessionManager {
    
    private static SessionManager manager  = null;
    private final ReentrantLock reentrantLock = new ReentrantLock();
    private SessionManager(){
    }
    
    public static SessionManager getManager(){
        if (manager == null) {
            manager = new SessionManager();
        }
        return manager;
    }
    
    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, String> playerToSession = new ConcurrentHashMap<>();

    public void register(Session session) {
        reentrantLock.lock();
        try {
            sessions.put(session.getId(), session);
            playerToSession.put(session.getPlayer1().getPlayerId(), session.getId());
            playerToSession.put(session.getPlayer2().getPlayerId(), session.getId());
        } finally {
            reentrantLock.unlock();
        }
    }

    public Session getByPlayer(int playerId) {
        reentrantLock.lock();
        try {
            
            String sessionId = playerToSession.get(playerId);
            return sessionId == null ? null : sessions.get(sessionId);
        } finally {
            reentrantLock.unlock();
        }
    }

    public void remove(Session session) {
        reentrantLock.lock();
        try {
            sessions.remove(session.getId());
            playerToSession.remove(session.getPlayer1().getPlayerId());
            playerToSession.remove(session.getPlayer2().getPlayerId());
        } finally {
            reentrantLock.unlock();
        }
        
    }
    
    public int getSessionsCount(){
        return sessions.values().stream().filter(Session::isActive).toList().size();
    }
}