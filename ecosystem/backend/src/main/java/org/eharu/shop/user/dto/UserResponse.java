package org.eharu.shop.user.dto;

import org.eharu.shop.user.domain.User;
import org.eharu.shop.user.domain.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String username,
        UserRole role,
        boolean isActive,
        LocalDateTime createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt()
        );
    }
}
