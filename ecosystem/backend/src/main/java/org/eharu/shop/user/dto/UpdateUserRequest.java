package org.eharu.shop.user.dto;

import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(min = 3, max = 50) String username,
        Boolean isActive
) {}
