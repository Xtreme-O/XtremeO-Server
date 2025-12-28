package org.example.xtremo.mapper;

import org.example.xtremo.model.dto.ScoreDTO;
import org.example.xtremo.model.entity.PlayerScore;

public class PlayerScoreMapper {

    private PlayerScoreMapper() {
    }

    public static ScoreDTO toDto(PlayerScore userScore) {
        if (userScore == null) {
            return null;
        }
        return new ScoreDTO(
            userScore.getScoreId(),
            userScore.getUserId(),
            userScore.getGameType(),
            userScore.getWins(),
            userScore.getLosses(),
            userScore.getDraws(),
            userScore.getLongestStreak()
        );
    }

    public static PlayerScore toEntity(ScoreDTO scoreDTO) {
        if (scoreDTO == null) {
            return null;
        }
        PlayerScore userScore = new PlayerScore();
        userScore.setScoreId(scoreDTO.scoreId());
        userScore.setUserId(scoreDTO.userId());
        userScore.setGameType(scoreDTO.gameType());
        userScore.setWins(scoreDTO.wins());
        userScore.setLosses(scoreDTO.losses());
        userScore.setDraws(scoreDTO.draws());
        userScore.setLongestStreak(scoreDTO.longestStreak());
        return userScore;
    }
}
