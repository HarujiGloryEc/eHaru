package org.eharu.shop.category.application;

import org.eharu.shop.category.domain.Category;
import org.eharu.shop.category.domain.CategoryRepository;
import org.eharu.shop.category.dto.CategoryResponse;
import org.eharu.shop.category.dto.CreateCategoryRequest;
import org.eharu.shop.category.dto.UpdateCategoryRequest;
import org.eharu.shop.shared.exception.ResourceNotFoundException;
import org.eharu.shop.store.domain.Store;
import org.eharu.shop.store.domain.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void createCategory_success() {
        UUID storeId = UUID.randomUUID();
        Store store = storeWithId(storeId);

        CreateCategoryRequest request = new CreateCategoryRequest(
                storeId, null, "Electronics", "electronics", null, 0);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(categoryRepository.existsByStoreIdAndSlug(storeId, "electronics")).thenReturn(false);

        Category saved = categoryWith(UUID.randomUUID(), store, null, "Electronics", "electronics");
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        CategoryResponse response = categoryService.createCategory(request);

        assertThat(response.name()).isEqualTo("Electronics");
        assertThat(response.slug()).isEqualTo("electronics");
        assertThat(response.storeId()).isEqualTo(storeId);
        assertThat(response.parentId()).isNull();
    }

    @Test
    void createCategory_storeNotFound_throwsResourceNotFound() {
        UUID storeId = UUID.randomUUID();
        CreateCategoryRequest request = new CreateCategoryRequest(
                storeId, null, "Electronics", "electronics", null, 0);

        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.createCategory(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Store");
    }

    @Test
    void createCategory_duplicateSlug_throwsIllegalArgument() {
        UUID storeId = UUID.randomUUID();
        Store store = storeWithId(storeId);
        CreateCategoryRequest request = new CreateCategoryRequest(
                storeId, null, "Electronics", "electronics", null, 0);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(categoryRepository.existsByStoreIdAndSlug(storeId, "electronics")).thenReturn(true);

        assertThatThrownBy(() -> categoryService.createCategory(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("electronics");
    }

    @Test
    void createCategory_withParent_success() {
        UUID storeId = UUID.randomUUID();
        UUID parentId = UUID.randomUUID();
        Store store = storeWithId(storeId);
        Category parent = categoryWith(parentId, store, null, "Electronics", "electronics");

        CreateCategoryRequest request = new CreateCategoryRequest(
                storeId, parentId, "Phones", "phones", null, 0);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(categoryRepository.existsByStoreIdAndSlug(storeId, "phones")).thenReturn(false);
        when(categoryRepository.findById(parentId)).thenReturn(Optional.of(parent));

        Category saved = categoryWith(UUID.randomUUID(), store, parent, "Phones", "phones");
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        CategoryResponse response = categoryService.createCategory(request);

        assertThat(response.parentId()).isEqualTo(parentId);
        assertThat(response.name()).isEqualTo("Phones");
    }

    @Test
    void getCategoryById_notFound_throwsResourceNotFound() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category");
    }

    @Test
    void getRootCategories_success() {
        UUID storeId = UUID.randomUUID();
        Store store = storeWithId(storeId);
        Category root1 = categoryWith(UUID.randomUUID(), store, null, "Electronics", "electronics");
        Category root2 = categoryWith(UUID.randomUUID(), store, null, "Clothing", "clothing");

        when(categoryRepository.findByStoreIdAndParentIsNull(storeId)).thenReturn(List.of(root1, root2));

        List<CategoryResponse> responses = categoryService.getRootCategories(storeId);

        assertThat(responses).hasSize(2);
        assertThat(responses).extracting(CategoryResponse::parentId).containsOnly((UUID) null);
    }

    @Test
    void getChildCategories_success() {
        UUID parentId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Store store = storeWithId(storeId);

        Category parent = categoryWith(parentId, store, null, "Electronics", "electronics");
        Category child1 = categoryWith(UUID.randomUUID(), store, parent, "Phones", "phones");
        Category child2 = categoryWith(UUID.randomUUID(), store, parent, "Laptops", "laptops");

        when(categoryRepository.findByParentId(parentId)).thenReturn(List.of(child1, child2));

        List<CategoryResponse> responses = categoryService.getChildCategories(parentId);

        assertThat(responses).hasSize(2);
        assertThat(responses).extracting(CategoryResponse::parentId).containsOnly(parentId);
    }

    @Test
    void updateCategory_success() {
        UUID id = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Store store = storeWithId(storeId);
        Category existing = categoryWith(id, store, null, "Old Name", "old-slug");

        UpdateCategoryRequest request = new UpdateCategoryRequest("New Name", null, "A description", 5, false);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        CategoryResponse response = categoryService.updateCategory(id, request);

        assertThat(response.name()).isEqualTo("New Name");
        assertThat(response.description()).isEqualTo("A description");
        assertThat(response.displayOrder()).isEqualTo(5);
        assertThat(response.isActive()).isFalse();
    }

    @Test
    void deleteCategory_success() {
        UUID id = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Store store = storeWithId(storeId);
        Category category = categoryWith(id, store, null, "Electronics", "electronics");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(id);

        verify(categoryRepository).delete(category);
    }

    private Store storeWithId(UUID id) {
        Store store = new Store();
        store.setId(id);
        store.setName("Test Store");
        store.setSlug("test-store");
        return store;
    }

    private Category categoryWith(UUID id, Store store, Category parent, String name, String slug) {
        Category category = new Category();
        category.setId(id);
        category.setStore(store);
        category.setParent(parent);
        category.setName(name);
        category.setSlug(slug);
        category.setDisplayOrder(0);
        category.setActive(true);
        return category;
    }
}
