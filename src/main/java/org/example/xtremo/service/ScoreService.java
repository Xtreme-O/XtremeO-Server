package org.example.xtremo.service;

import org.example.xtremo.dao.PlayerDao;
import org.example.xtremo.dao.PlayerDaoImpl;
import org.example.xtremo.dao.PlayerScoreDao;
import org.example.xtremo.dao.PlayerScoreDaoImpl;
import org.example.xtremo.database.DBConnection;
import org.example.xtremo.mapper.PlayerMapper;
import org.example.xtremo.mapper.PlayerScoreMapper;
import org.example.xtremo.model.dto.PlayerDTO;
import org.example.xtremo.model.dto.PlayerScoreDTO;
import org.example.xtremo.model.dto.ScoreDTO;
import org.example.xtremo.model.entity.Player;
import org.example.xtremo.model.entity.PlayerScore;
import org.example.xtremo.model.enums.GameResult;
import org.example.xtremo.model.enums.GameType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreService {

    private final PlayerScoreDao playerScoreDAO;
    private final PlayerDao playerDAO;

    private static ScoreService instance;
    public ScoreService() throws SQLException {
        this.playerScoreDAO = new PlayerScoreDaoImpl(DBConnection.getConnection());
        this.playerDAO = new PlayerDaoImpl(DBConnection.getConnection());
    }

    public static ScoreService getInstance() throws SQLException {
        if(instance == null) {
            instance = new ScoreService();
        }
        return instance;
    }


    public PlayerScoreDTO getPlayerScore(int playerID){
        Player player = playerDAO.findById(playerID).orElseThrow(()-> new RuntimeException("Player not found!"));
        PlayerScore score = playerScoreDAO.findByUserId(playerID).orElse(null);
        return new PlayerScoreDTO(PlayerMapper.toDto(player), PlayerScoreMapper.toDto(score), calculateScore(score));
    }


    public void updateGameResult(GameResult gameResult, GameType type,int playerID){

        var score = playerScoreDAO.findByUserIdAndGameType(playerID , type)
                .orElseThrow(()->new RuntimeException("Score not found!"));
        switch (gameResult){
            case WIN -> {
                score.setWins(score.getWins()+1);
            }
            case DRAW -> {
                score.setDraws(score.getDraws()+1);
            }
            default -> {
                score.setLosses(score.getLosses()+1);
            }
        }
        playerScoreDAO.update(score);
    }

    public List<PlayerScoreDTO> getAllPlayerScores(){

        List<Player> players = playerDAO.findAll();
        List<PlayerScore> scores = playerScoreDAO.findAll();
        List<PlayerScoreDTO> playerScoresDTO = new ArrayList<>();
        for (Player player : players){
            var playerScores =
                    scores.stream().filter(s-> s.getUserId() == player.getId()).toList();
            if(!playerScores.isEmpty()){
                playerScoresDTO.add(
                        new PlayerScoreDTO(
                                PlayerMapper.toDto(player),
                                PlayerScoreMapper.toDto(playerScores.getFirst()),
                                calculateScore(playerScores.getFirst())
                        )
                );
            }
        }
        playerScoresDTO.sort(Comparator.comparing(PlayerScoreDTO::elo).reversed());
        return playerScoresDTO.stream().toList();
    }

    private int calculateScore(PlayerScore score){
        if(score == null)
            return 0;
        return score.getWins() * 3 + score.getDraws();
    }

}
