package org.example.xtremo.model.dto;

import com.google.gson.JsonObject;

public record MoveDTO(
    int moveId,
    int gameId,
    int playerId,
    int moveNumber,
    JsonObject moveData
) {
}
