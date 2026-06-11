package org.eharu.shop.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record CreateStoreRequest(
        @NotBlank String name,
        @NotBlank @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, digits, and hyphens") String slug,
        String description,
        @NotNull UUID merchantId
) {}
