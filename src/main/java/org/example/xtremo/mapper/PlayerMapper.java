package org.example.xtremo.mapper;

import org.example.xtremo.model.dto.PlayerDto;
import org.example.xtremo.model.entity.Player;

public class PlayerMapper {

    private PlayerMapper() {
    }

    public static PlayerDto toDto(Player player) {
        if (player == null) {
            return null;
        }
        return new PlayerDto(
            player.getId(),
            player.getUsername(),
            player.getAvatarUrl(),
            player.getStatus(),
            player.getCreatedAt(),
            player.getLastLogin()
        );
    }

    public static Player toEntity(PlayerDto playerDto) {
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
