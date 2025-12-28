package org.example.xtremo.model.dto;

public record ScoreDTO(
    int scoreId,
    int userId,
    String gameType,
    int wins,
    int losses,
    int draws,
    int longestStreak
) {
}
