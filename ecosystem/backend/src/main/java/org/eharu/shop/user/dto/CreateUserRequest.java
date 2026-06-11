package org.eharu.shop.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eharu.shop.user.domain.UserRole;

public record CreateUserRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Size(min = 8) String password,
        UserRole role
) {
    public CreateUserRequest {
        if (role == null) {
            role = UserRole.CUSTOMER;
        }
    }
}
