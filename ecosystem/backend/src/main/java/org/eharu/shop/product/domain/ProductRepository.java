package org.eharu.shop.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByStoreId(UUID storeId);
    List<Product> findByStoreIdAndStatus(UUID storeId, ProductStatus status);
    List<Product> findByCategoryId(UUID categoryId);
    Optional<Product> findByStoreIdAndSlug(UUID storeId, String slug);
    boolean existsByStoreIdAndSlug(UUID storeId, String slug);
}
