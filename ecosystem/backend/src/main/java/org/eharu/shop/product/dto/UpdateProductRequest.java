package org.eharu.shop.product.dto;

import org.eharu.shop.product.domain.ProductStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductRequest(
        String name,
        String slug,
        String description,
        String sku,
        String primaryImageUrl,
        BigDecimal price,
        BigDecimal compareAtPrice,
        BigDecimal costPrice,
        Integer stockQuantity,
        Integer lowStockThreshold,
        Boolean trackInventory,
        ProductStatus status,
        UUID categoryId
) {
}
