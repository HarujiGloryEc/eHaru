package org.eharu.shop.category.application;

import org.eharu.shop.category.domain.Category;
import org.eharu.shop.category.domain.CategoryRepository;
import org.eharu.shop.category.dto.CategoryResponse;
import org.eharu.shop.category.dto.CreateCategoryRequest;
import org.eharu.shop.category.dto.UpdateCategoryRequest;
import org.eharu.shop.shared.exception.ResourceNotFoundException;
import org.eharu.shop.store.domain.Store;
import org.eharu.shop.store.domain.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, StoreRepository storeRepository) {
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new ResourceNotFoundException("Store", "id", request.storeId()));

        if (categoryRepository.existsByStoreIdAndSlug(request.storeId(), request.slug())) {
            throw new IllegalArgumentException(
                    "Slug '" + request.slug() + "' already exists in store " + request.storeId());
        }

        Category category = new Category();
        category.setStore(store);
        category.setName(request.name());
        category.setSlug(request.slug());
        category.setDescription(request.description());
        category.setDisplayOrder(request.displayOrder());

        if (request.parentId() != null) {
            Category parent = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.parentId()));
            category.setParent(parent);
        }

        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse getCategoryById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return CategoryResponse.from(category);
    }

    @Override
    public List<CategoryResponse> getCategoriesByStore(UUID storeId) {
        return categoryRepository.findByStoreId(storeId).stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Override
    public List<CategoryResponse> getRootCategories(UUID storeId) {
        return categoryRepository.findByStoreIdAndParentIsNull(storeId).stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Override
    public List<CategoryResponse> getChildCategories(UUID parentId) {
        return categoryRepository.findByParentId(parentId).stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Override
    public CategoryResponse updateCategory(UUID id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (request.name() != null) {
            category.setName(request.name());
        }
        if (request.description() != null) {
            category.setDescription(request.description());
        }
        if (request.displayOrder() != null) {
            category.setDisplayOrder(request.displayOrder());
        }
        if (request.isActive() != null) {
            category.setActive(request.isActive());
        }
        if (request.parentId() != null) {
            Category parent = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.parentId()));
            category.setParent(parent);
        }

        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        categoryRepository.delete(category);
    }
}
