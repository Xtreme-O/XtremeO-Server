package org.example.xtremo.model.entity;

public class GameMove {
    private int moveId;
    private int gameId;
    private int playerId;
    private int moveNumber;
    private String moveData;

    public GameMove() {
    }

    public GameMove(int moveId, int gameId, int playerId, int moveNumber, String moveData) {
        this.moveId = moveId;
        this.gameId = gameId;
        this.playerId = playerId;
        this.moveNumber = moveNumber;
        this.moveData = moveData;
    }

    public int getMoveId() {
        return moveId;
    }

    public void setMoveId(int moveId) {
        this.moveId = moveId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public String getMoveData() {
        return moveData;
    }

    public void setMoveData(String moveData) {
        this.moveData = moveData;
    }
}
