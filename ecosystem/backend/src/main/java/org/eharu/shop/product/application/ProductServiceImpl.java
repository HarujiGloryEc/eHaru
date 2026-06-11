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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              StoreRepository storeRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new ResourceNotFoundException("Store", "id", request.storeId()));

        Category category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.categoryId()));
        }

        if (productRepository.existsByStoreIdAndSlug(request.storeId(), request.slug())) {
            throw new IllegalArgumentException(
                    "Product with slug '" + request.slug() + "' already exists in this store");
        }

        Product product = new Product();
        product.setStore(store);
        product.setCategory(category);
        product.setName(request.name());
        product.setSlug(request.slug());
        product.setDescription(request.description());
        product.setSku(request.sku());
        product.setPrice(request.price());
        product.setCompareAtPrice(request.compareAtPrice());
        product.setCostPrice(request.costPrice());
        product.setStockQuantity(request.stockQuantity());
        product.setLowStockThreshold(request.lowStockThreshold());
        product.setTrackInventory(request.trackInventory());
        product.setStatus(request.status() != null ? request.status() : ProductStatus.DRAFT);
        product.setPrimaryImageUrl(request.primaryImageUrl());

        return ProductResponse.from(productRepository.save(product));
    }

    @Override
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return ProductResponse.from(product);
    }

    @Override
    public List<ProductResponse> getProductsByStore(UUID storeId) {
        return productRepository.findByStoreId(storeId).stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByStoreAndStatus(UUID storeId, ProductStatus status) {
        return productRepository.findByStoreIdAndStatus(storeId, status).stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategory(UUID categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Override
    public ProductResponse updateProduct(UUID id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        if (request.name() != null) product.setName(request.name());
        if (request.slug() != null) product.setSlug(request.slug());
        if (request.description() != null) product.setDescription(request.description());
        if (request.sku() != null) product.setSku(request.sku());
        if (request.primaryImageUrl() != null) product.setPrimaryImageUrl(request.primaryImageUrl());
        if (request.price() != null) product.setPrice(request.price());
        if (request.compareAtPrice() != null) product.setCompareAtPrice(request.compareAtPrice());
        if (request.costPrice() != null) product.setCostPrice(request.costPrice());
        if (request.stockQuantity() != null) product.setStockQuantity(request.stockQuantity());
        if (request.lowStockThreshold() != null) product.setLowStockThreshold(request.lowStockThreshold());
        if (request.trackInventory() != null) product.setTrackInventory(request.trackInventory());
        if (request.status() != null) product.setStatus(request.status());

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.categoryId()));
            product.setCategory(category);
        }

        return ProductResponse.from(productRepository.save(product));
    }

    @Override
    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(product);
    }
}
