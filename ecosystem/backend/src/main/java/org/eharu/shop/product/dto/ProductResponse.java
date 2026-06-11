package org.eharu.shop.product.dto;

import org.eharu.shop.product.domain.Product;
import org.eharu.shop.product.domain.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        UUID storeId,
        UUID categoryId,
        String name,
        String slug,
        String description,
        String sku,
        BigDecimal price,
        BigDecimal compareAtPrice,
        BigDecimal costPrice,
        int stockQuantity,
        int lowStockThreshold,
        boolean trackInventory,
        ProductStatus status,
        String primaryImageUrl,
        LocalDateTime createdAt
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getStore().getId(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getSku(),
                product.getPrice(),
                product.getCompareAtPrice(),
                product.getCostPrice(),
                product.getStockQuantity(),
                product.getLowStockThreshold(),
                product.isTrackInventory(),
                product.getStatus(),
                product.getPrimaryImageUrl(),
                product.getCreatedAt()
        );
    }
}
