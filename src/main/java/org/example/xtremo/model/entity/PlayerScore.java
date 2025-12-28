package org.example.xtremo.model.entity;

public class PlayerScore {

    private int scoreId;
    private int userId;
    private String gameType;
    private int wins;
    private int losses;
    private int draws;
    private int longestStreak;

    public PlayerScore(int scoreId, int userId, String gameType, int wins, int losses, int draws, int longestStreak) {
        this.scoreId = scoreId;
        this.userId = userId;
        this.gameType = gameType;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.longestStreak = longestStreak;
    }

    public PlayerScore() {
    }

    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }
}
