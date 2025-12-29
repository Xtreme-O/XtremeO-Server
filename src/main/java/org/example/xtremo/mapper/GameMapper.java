package org.example.xtremo.mapper;

import org.example.xtremo.model.dto.GameDTO;
import org.example.xtremo.model.entity.Game;

public class GameMapper {

    private GameMapper() {
        // Private constructor to prevent instantiation
    }

    public static GameDTO toDto(Game game) {
        if (game == null) {
            return null;
        }
        return new GameDTO(
            game.getGameId(),
            game.getGameType(),
            game.getPlayer1Id(),
            game.getPlayer2Id(),
            game.getWinnerId(),
            game.getGameResult(),
            game.getStartedAt(),
            game.getEndedAt(),
            game.isRecorded(),
            game.getRecordFilePath()
        );
    }

    public static Game toEntity(GameDTO gameDTO) {
        if (gameDTO == null) {
            return null;
        }
        return new Game(
            gameDTO.gameId(),
            gameDTO.gameType(),
            gameDTO.player1Id(),
            gameDTO.player2Id(),
            gameDTO.winnerId(),
            gameDTO.gameResult(),
            gameDTO.startedAt(),
            gameDTO.endedAt(),
            gameDTO.isRecorded(),
            gameDTO.recordFilePath()
        );
    }
}
