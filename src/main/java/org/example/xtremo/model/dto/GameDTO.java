package org.example.xtremo.model.dto;

import java.time.LocalDateTime;

public record GameDTO(
    int gameId,
    String gameType,
    int player1Id,
    Integer player2Id,
    Integer winnerId,
    LocalDateTime startedAt,
    LocalDateTime endedAt,
    boolean isRecorded,
    String recordFilePath
) {
}
