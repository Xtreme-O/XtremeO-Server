package org.example.xtremo.model.entity;

import java.time.LocalDateTime;

public class Game {
    private int gameId;
    private String gameType;
    private int player1Id;
    private Integer player2Id;
    private Integer winnerId;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private boolean isRecorded;

    public Game(int gameId, String gameType, int player1Id, Integer player2Id, Integer winnerId, LocalDateTime startedAt, LocalDateTime endedAt, boolean isRecorded, String recordFilePath) {
        this.gameId = gameId;
        this.gameType = gameType;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.winnerId = winnerId;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.isRecorded = isRecorded;
        this.recordFilePath = recordFilePath;
    }
    private String recordFilePath;

    public Game() {
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public int getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(int player1Id) {
        this.player1Id = player1Id;
    }

    public Integer getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Integer player2Id) {
        this.player2Id = player2Id;
    }

    public Integer getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Integer winnerId) {
        this.winnerId = winnerId;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public boolean isRecorded() {
        return isRecorded;
    }

    public void setRecorded(boolean recorded) {
        isRecorded = recorded;
    }

    public String getRecordFilePath() {
        return recordFilePath;
    }

    public void setRecordFilePath(String recordFilePath) {
        this.recordFilePath = recordFilePath;
    }
}
