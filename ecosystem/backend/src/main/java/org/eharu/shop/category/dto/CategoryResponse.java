package org.eharu.shop.category.dto;

import org.eharu.shop.category.domain.Category;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        String slug,
        String description,
        int displayOrder,
        boolean isActive,
        UUID storeId,
        UUID parentId,
        LocalDateTime createdAt
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getDisplayOrder(),
                category.isActive(),
                category.getStore().getId(),
                category.getParent() != null ? category.getParent().getId() : null,
                category.getCreatedAt()
        );
    }
}
