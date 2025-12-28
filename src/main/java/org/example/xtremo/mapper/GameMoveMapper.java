package org.example.xtremo.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.xtremo.model.dto.MoveDTO;
import org.example.xtremo.model.entity.GameMove;

public class GameMoveMapper {

    private static final Gson gson = new Gson();

    private GameMoveMapper() {
    }

    public static MoveDTO toDto(GameMove gameMove) {
        if (gameMove == null) {
            return null;
        }
        JsonObject moveData = null;
        if (gameMove.getMoveData() != null) {
            moveData = gson.fromJson(gameMove.getMoveData(), JsonObject.class);
        }
        return new MoveDTO(
            gameMove.getMoveId(),
            gameMove.getGameId(),
            gameMove.getPlayerId(),
            gameMove.getMoveNumber(),
            moveData
        );
    }

    public static GameMove toEntity(MoveDTO moveDTO) {
        if (moveDTO == null) {
            return null;
        }
        GameMove gameMove = new GameMove();
        gameMove.setMoveId(moveDTO.moveId());
        gameMove.setGameId(moveDTO.gameId());
        gameMove.setPlayerId(moveDTO.playerId());
        gameMove.setMoveNumber(moveDTO.moveNumber());
        if (moveDTO.moveData() != null) {
            gameMove.setMoveData(gson.toJson(moveDTO.moveData()));
        }
        return gameMove;
    }
}
