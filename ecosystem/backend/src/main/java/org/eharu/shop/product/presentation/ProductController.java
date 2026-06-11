package org.eharu.shop.product.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.eharu.shop.product.application.ProductService;
import org.eharu.shop.product.domain.ProductStatus;
import org.eharu.shop.product.dto.CreateProductRequest;
import org.eharu.shop.product.dto.ProductResponse;
import org.eharu.shop.product.dto.UpdateProductRequest;
import org.eharu.shop.shared.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Product management endpoints")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.of(productService.getProductById(id)));
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        ProductResponse created = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable UUID id,
            @RequestBody UpdateProductRequest request) {
        return ResponseEntity.ok(ApiResponse.of(productService.updateProduct(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Get all products for a store")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByStore(
            @PathVariable UUID storeId) {
        return ResponseEntity.ok(ApiResponse.of(productService.getProductsByStore(storeId)));
    }

    @GetMapping("/store/{storeId}/status/{status}")
    @Operation(summary = "Get products for a store filtered by status")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByStoreAndStatus(
            @PathVariable UUID storeId,
            @PathVariable ProductStatus status) {
        return ResponseEntity.ok(ApiResponse.of(productService.getProductsByStoreAndStatus(storeId, status)));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get all products in a category")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(
            @PathVariable UUID categoryId) {
        return ResponseEntity.ok(ApiResponse.of(productService.getProductsByCategory(categoryId)));
    }
}
