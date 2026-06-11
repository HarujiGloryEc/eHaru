package org.eharu.shop.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCategoryRequest(
        @NotNull UUID storeId,
        UUID parentId,
        @NotBlank String name,
        @NotBlank String slug,
        String description,
        int displayOrder
) {}
