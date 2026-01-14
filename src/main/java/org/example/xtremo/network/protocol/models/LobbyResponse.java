package org.example.xtremo.network.protocol.models;

import org.example.xtremo.model.dto.PlayerScoreDTO;

import java.util.List;

public record LobbyResponse(List<PlayerScoreDTO> activeUsers, List<PlayerScoreDTO> playersScores) {


}

