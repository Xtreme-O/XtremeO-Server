package org.example.xtremo.mapper;

import org.example.xtremo.model.dto.GameDTO;
import org.example.xtremo.model.entity.Game;

public class GameMapper {

    private GameMapper() {
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
        Game game = new Game();
        game.setGameId(gameDTO.gameId());
        game.setGameType(gameDTO.gameType());
        game.setPlayer1Id(gameDTO.player1Id());
        game.setPlayer2Id(gameDTO.player2Id());
        game.setWinnerId(gameDTO.winnerId());
        game.setStartedAt(gameDTO.startedAt());
        game.setEndedAt(gameDTO.endedAt());
        game.setRecorded(gameDTO.isRecorded());
        game.setRecordFilePath(gameDTO.recordFilePath());
        return game;
    }
}
