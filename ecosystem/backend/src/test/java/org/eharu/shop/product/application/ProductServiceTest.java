package org.eharu.shop.product.application;

import org.eharu.shop.category.domain.Category;
import org.eharu.shop.category.domain.CategoryRepository;
import org.eharu.shop.product.domain.Product;
import org.eharu.shop.product.domain.ProductRepository;
import org.eharu.shop.product.domain.ProductStatus;
import org.eharu.shop.product.dto.CreateProductRequest;
import org.eharu.shop.product.dto.ProductResponse;
import org.eharu.shop.product.dto.UpdateProductRequest;
import org.eharu.shop.shared.exception.ResourceNotFoundException;
import org.eharu.shop.store.domain.Store;
import org.eharu.shop.store.domain.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Store buildStore(UUID id) {
        Store store = new Store();
        store.setId(id);
        store.setName("Test Store");
        store.setSlug("test-store");
        return store;
    }

    private Category buildCategory(UUID id) {
        Category category = new Category();
        category.setId(id);
        category.setName("Test Category");
        category.setSlug("test-category");
        return category;
    }

    private Product buildProduct(UUID id, Store store, Category category) {
        Product product = new Product();
        product.setId(id);
        product.setStore(store);
        product.setCategory(category);
        product.setName("Test Product");
        product.setSlug("test-product");
        product.setPrice(new BigDecimal("19.99"));
        product.setStockQuantity(10);
        product.setLowStockThreshold(5);
        product.setTrackInventory(true);
        product.setStatus(ProductStatus.DRAFT);
        return product;
    }

    private CreateProductRequest buildCreateRequest(UUID storeId, UUID categoryId) {
        return new CreateProductRequest(
                storeId,
                categoryId,
                "Test Product",
                "test-product",
                null,
                null,
                new BigDecimal("19.99"),
                null,
                null,
                0,
                5,
                true,
                null,
                null
        );
    }

    @Test
    void createProduct_success() {
        UUID storeId = UUID.randomUUID();
        Store store = buildStore(storeId);
        CreateProductRequest request = buildCreateRequest(storeId, null);

        UUID productId = UUID.randomUUID();
        Product saved = buildProduct(productId, store, null);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(productRepository.existsByStoreIdAndSlug(storeId, "test-product")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductResponse response = productService.createProduct(request);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        Product captured = captor.getValue();
        assertThat(captured.getName()).isEqualTo("Test Product");
        assertThat(captured.getSlug()).isEqualTo("test-product");
        assertThat(captured.getStatus()).isEqualTo(ProductStatus.DRAFT);
        assertThat(response.id()).isEqualTo(productId);
    }

    @Test
    void createProduct_storeNotFound_throwsResourceNotFound() {
        UUID storeId = UUID.randomUUID();
        CreateProductRequest request = buildCreateRequest(storeId, null);

        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Store not found with id");
    }

    @Test
    void createProduct_duplicateSlug_throwsIllegalArgument() {
        UUID storeId = UUID.randomUUID();
        Store store = buildStore(storeId);
        CreateProductRequest request = buildCreateRequest(storeId, null);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(productRepository.existsByStoreIdAndSlug(storeId, "test-product")).thenReturn(true);

        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("test-product");
    }

    @Test
    void createProduct_withCategory_success() {
        UUID storeId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        Store store = buildStore(storeId);
        Category category = buildCategory(categoryId);
        CreateProductRequest request = buildCreateRequest(storeId, categoryId);

        UUID productId = UUID.randomUUID();
        Product saved = buildProduct(productId, store, category);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.existsByStoreIdAndSlug(storeId, "test-product")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductResponse response = productService.createProduct(request);

        assertThat(response.categoryId()).isEqualTo(categoryId);
    }

    @Test
    void getProductById_notFound_throwsResourceNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found with id");
    }

    @Test
    void getProductsByStore_success() {
        UUID storeId = UUID.randomUUID();
        Store store = buildStore(storeId);
        Product p1 = buildProduct(UUID.randomUUID(), store, null);
        Product p2 = buildProduct(UUID.randomUUID(), store, null);

        when(productRepository.findByStoreId(storeId)).thenReturn(List.of(p1, p2));

        List<ProductResponse> results = productService.getProductsByStore(storeId);

        assertThat(results).hasSize(2);
        verify(productRepository).findByStoreId(storeId);
    }

    @Test
    void getProductsByStoreAndStatus_success() {
        UUID storeId = UUID.randomUUID();
        Store store = buildStore(storeId);
        Product p = buildProduct(UUID.randomUUID(), store, null);
        p.setStatus(ProductStatus.ACTIVE);

        when(productRepository.findByStoreIdAndStatus(storeId, ProductStatus.ACTIVE))
                .thenReturn(List.of(p));

        List<ProductResponse> results = productService.getProductsByStoreAndStatus(storeId, ProductStatus.ACTIVE);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).status()).isEqualTo(ProductStatus.ACTIVE);
    }

    @Test
    void updateProduct_success() {
        UUID id = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Store store = buildStore(storeId);
        Product existing = buildProduct(id, store, null);

        UpdateProductRequest request = new UpdateProductRequest(
                "Updated Name", null, null, null, null,
                new BigDecimal("29.99"), null, null,
                null, null, null,
                ProductStatus.ACTIVE, null
        );

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductResponse response = productService.updateProduct(id, request);

        assertThat(response.name()).isEqualTo("Updated Name");
        assertThat(response.price()).isEqualByComparingTo(new BigDecimal("29.99"));
        assertThat(response.status()).isEqualTo(ProductStatus.ACTIVE);
        verify(productRepository).save(existing);
    }

    @Test
    void deleteProduct_success() {
        UUID id = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        Store store = buildStore(storeId);
        Product existing = buildProduct(id, store, null);

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));

        productService.deleteProduct(id);

        verify(productRepository).delete(existing);
    }
}
