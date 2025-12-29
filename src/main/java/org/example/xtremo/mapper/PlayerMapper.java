package org.example.xtremo.mapper;

import org.example.xtremo.model.dto.PlayerDTO;
import org.example.xtremo.model.entity.Player;

public class PlayerMapper {

    private PlayerMapper() {
    }

    public static PlayerDTO toDto(Player player) {
        if (player == null) {
            return null;
        }
        return new PlayerDTO(
            player.getId(),
            player.getUsername(),
            player.getAvatarUrl(),
            player.getStatus(),
            player.getCreatedAt(),
            player.getLastLogin()
        );
    }

    public static Player toEntity(PlayerDTO playerDto) {
        if (playerDto == null) {
            return null;
        }
        Player player = new Player();
        player.setId(playerDto.id());
        player.setUsername(playerDto.username());
        player.setAvatarUrl(playerDto.avatarUrl());
        player.setStatus(playerDto.status());
        player.setCreatedAt(playerDto.createdAt());
        player.setLastLogin(playerDto.lastLogin());
        return player;
    }
}
