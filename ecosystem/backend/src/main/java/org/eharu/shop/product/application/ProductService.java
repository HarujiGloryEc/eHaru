package org.eharu.shop.product.application;

import org.eharu.shop.product.domain.ProductStatus;
import org.eharu.shop.product.dto.CreateProductRequest;
import org.eharu.shop.product.dto.ProductResponse;
import org.eharu.shop.product.dto.UpdateProductRequest;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse getProductById(UUID id);
    List<ProductResponse> getProductsByStore(UUID storeId);
    List<ProductResponse> getProductsByStoreAndStatus(UUID storeId, ProductStatus status);
    List<ProductResponse> getProductsByCategory(UUID categoryId);
    ProductResponse updateProduct(UUID id, UpdateProductRequest request);
    void deleteProduct(UUID id);
}
