package org.example.xtremo.model.dto;

import java.time.LocalDateTime;

public record PlayerDto(
    int id,
    String username,
    String avatarUrl,
    String status,
    LocalDateTime createdAt,
    LocalDateTime lastLogin
) {
}
