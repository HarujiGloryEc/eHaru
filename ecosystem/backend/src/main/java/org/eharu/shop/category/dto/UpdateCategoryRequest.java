package org.eharu.shop.category.dto;

import java.util.UUID;

public record UpdateCategoryRequest(
        String name,
        UUID parentId,
        String description,
        Integer displayOrder,
        Boolean isActive
) {}
