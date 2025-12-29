package org.example.xtremo.model.dto;

import java.time.LocalDateTime;
import org.example.xtremo.model.enums.GameResult;
import org.example.xtremo.model.enums.GameType;


public record GameDTO(
    int gameId,
    GameType gameType,
    int player1Id,
    Integer player2Id,
    Integer winnerId,
    GameResult gameResult,
    LocalDateTime startedAt,
    LocalDateTime endedAt,
    boolean isRecorded,
    String recordFilePath
) {}