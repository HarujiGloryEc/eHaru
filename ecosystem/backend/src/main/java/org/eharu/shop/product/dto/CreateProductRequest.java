package org.eharu.shop.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eharu.shop.product.domain.ProductStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductRequest(
        @NotNull UUID storeId,
        UUID categoryId,
        @NotBlank String name,
        @NotBlank String slug,
        String description,
        String sku,
        @NotNull @DecimalMin("0.00") BigDecimal price,
        BigDecimal compareAtPrice,
        BigDecimal costPrice,
        int stockQuantity,
        int lowStockThreshold,
        boolean trackInventory,
        ProductStatus status,
        String primaryImageUrl
) {
}
