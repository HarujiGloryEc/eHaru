package org.eharu.shop.category.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByStoreId(UUID storeId);
    List<Category> findByStoreIdAndParentIsNull(UUID storeId);
    List<Category> findByParentId(UUID parentId);
    Optional<Category> findByStoreIdAndSlug(UUID storeId, String slug);
    boolean existsByStoreIdAndSlug(UUID storeId, String slug);
}
