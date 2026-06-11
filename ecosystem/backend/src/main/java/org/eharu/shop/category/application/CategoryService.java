package org.eharu.shop.category.application;

import org.eharu.shop.category.dto.CategoryResponse;
import org.eharu.shop.category.dto.CreateCategoryRequest;
import org.eharu.shop.category.dto.UpdateCategoryRequest;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryRequest request);
    CategoryResponse getCategoryById(UUID id);
    List<CategoryResponse> getCategoriesByStore(UUID storeId);
    List<CategoryResponse> getRootCategories(UUID storeId);
    List<CategoryResponse> getChildCategories(UUID parentId);
    CategoryResponse updateCategory(UUID id, UpdateCategoryRequest request);
    void deleteCategory(UUID id);
}
