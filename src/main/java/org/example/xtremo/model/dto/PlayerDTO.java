package org.example.xtremo.model.dto;

import java.time.LocalDateTime;
import org.example.xtremo.model.enums.PlayerStatus;

public record PlayerDTO(
    int id,
    String username,
    String avatarUrl,
    PlayerStatus status,
    LocalDateTime createdAt,
    LocalDateTime lastLogin
) {
}
