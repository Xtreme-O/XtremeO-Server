/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.xtremo.session;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import org.example.xtremo.logging.LoggerManager;
import org.example.xtremo.network.PlayerNetworkOperations;
import org.example.xtremo.network.protocol.Action;
import org.example.xtremo.network.protocol.ProtocolMessageEnvelope;
import org.example.xtremo.network.protocol.RequestHeader;
import org.example.xtremo.network.protocol.models.ErrorBody;

/**
 *
 * @author wahid
 */
public class Session {

    private static final LoggerManager logger = LoggerManager.getInstance();

    public enum SessionState {
        CREATED,
        ACTIVE,
        CLOSING,
        CLOSED
    }

    private final String id;
    private final SessionPlayer player1;
    private final SessionPlayer player2;
    private final AtomicReference<SessionState> state = new AtomicReference<>(SessionState.CREATED);
    private final ReentrantLock sessionLock = new ReentrantLock();

    private volatile Integer disconnectedPlayerId = null;

    public Session(String id, SessionPlayer player1, SessionPlayer player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
    }

    public String getId() {
        return id;
    }

    public SessionPlayer getPlayer1() {
        return player1;
    }

    public SessionPlayer getPlayer2() {
        return player2;
    }

    public SessionState getState() {
        return state.get();
    }

    public boolean activate() {
        return state.compareAndSet(SessionState.CREATED, SessionState.ACTIVE);
    }

    public void forward(ProtocolMessageEnvelope<?> message, int fromPlayerId) throws IOException {
        sessionLock.lock();
        try {
            SessionState currentState = state.get();

            if (currentState != SessionState.ACTIVE) {
                throw new IllegalStateException("Cannot forward message - session state: " + currentState);
            }

            SessionPlayer target = getOtherPlayer(fromPlayerId);

            if (target == null || target.getHandler() == null) {
                throw new IllegalStateException("Target player not available");
            }

            try {
                PlayerNetworkOperations.sendResponse(
                        message,
                        target.getHandler().getDos());
            } catch (IOException e) {
                logger.error("Failed to forward message in session " + id + ": " + e.getMessage());
                // Trigger disconnect handling for the unreachable player
                onPlayerDisconnected(target.getPlayerId());
                throw e;
            }

        } finally {
            sessionLock.unlock();
        }
    }

    public void onPlayerDisconnected(int playerId) {
        sessionLock.lock();
        try {
            if (!state.compareAndSet(SessionState.ACTIVE, SessionState.CLOSING)
                    && !state.compareAndSet(SessionState.CREATED, SessionState.CLOSING)) {
                logger.info("Session " + id + " already closing/closed");
                return;
            }

            disconnectedPlayerId = playerId;
            logger.info("Player " + playerId + " disconnected from session " + id);

            SessionPlayer remainingPlayer = getOtherPlayer(playerId);

            if (remainingPlayer != null) {
                notifyDisconnect(remainingPlayer);
                closePlayerConnection(remainingPlayer);
            }

            state.set(SessionState.CLOSED);

        } finally {
            sessionLock.unlock();
        }
    }

    public void closeSession() {
        sessionLock.lock();
        try {
            SessionState currentState = state.get();

            if (currentState == SessionState.CLOSED) {
                return;
            }

            state.set(SessionState.CLOSING);

            notifySessionEnd(player1, "Session ended");
            notifySessionEnd(player2, "Session ended");

            closePlayerConnection(player1);
            closePlayerConnection(player2);

            state.set(SessionState.CLOSED);
            logger.info("Session " + id + " closed gracefully");

        } finally {
            sessionLock.unlock();
        }
    }

    public SessionPlayer getOtherPlayer(int playerId) {
        if (player1.getPlayerId() == playerId) {
            return player2;
        } else if (player2.getPlayerId() == playerId) {
            return player1;
        }
        return null;
    }

    private void notifyDisconnect(SessionPlayer player) {
        if (player == null || player.getHandler() == null) {
            return;
        }

        try {
            ProtocolMessageEnvelope<ErrorBody> msg = new ProtocolMessageEnvelope<>(
                    new RequestHeader("JSON", Action.PARTNER_DISCONNECTED.name()),
                    new ErrorBody("Other player disconnected"));

            PlayerNetworkOperations.sendResponse(
                    msg,
                    player.getHandler().getDos());
            logger.info("Notified player " + player.getPlayerId() + " of disconnect in session " + id);
        } catch (Exception e) {
            logger.warn("Failed to notify player " + player.getPlayerId() + " of disconnect: " + e.getMessage());
        }
    }

    private void notifySessionEnd(SessionPlayer player, String reason) {
        if (player == null || player.getHandler() == null) {
            return;
        }

        try {
            ProtocolMessageEnvelope<ErrorBody> msg = new ProtocolMessageEnvelope<>(
                    new RequestHeader("JSON", Action.SESSION_ENDED.name()),
                    new ErrorBody(reason));

            PlayerNetworkOperations.sendResponse(
                    msg,
                    player.getHandler().getDos());
        } catch (Exception e) {
            logger.warn("Failed to notify player " + player.getPlayerId() + " of session end: " + e.getMessage());
        }
    }

    private void closePlayerConnection(SessionPlayer player) {
        if (player == null || player.getHandler() == null) {
            return;
        }

        try {
            player.getHandler().getSocket().close();
            logger.info("Closed connection for player " + player.getPlayerId() + " in session " + id);
        } catch (Exception e) {
            logger.warn("Error closing connection for player " + player.getPlayerId() + ": " + e.getMessage());
        }
    }

    public boolean isActive() {
        return state.get() == SessionState.ACTIVE;
    }

    public boolean isClosed() {
        return state.get() == SessionState.CLOSED;
    }
}