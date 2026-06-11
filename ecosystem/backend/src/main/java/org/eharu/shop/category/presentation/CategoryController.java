package org.eharu.shop.category.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.eharu.shop.category.application.CategoryService;
import org.eharu.shop.category.dto.CategoryResponse;
import org.eharu.shop.category.dto.CreateCategoryRequest;
import org.eharu.shop.category.dto.UpdateCategoryRequest;
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
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "Category management")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    @Operation(summary = "List all categories (placeholder — prefer store-scoped endpoints)")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> listAll() {
        return ResponseEntity.ok(ApiResponse.of(List.of()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.of(categoryService.getCategoryById(id)));
    }

    @PostMapping("/")
    @Operation(summary = "Create a new category")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable UUID id,
            @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.of(categoryService.updateCategory(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Get all categories for a store")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategoriesByStore(
            @PathVariable UUID storeId) {
        return ResponseEntity.ok(ApiResponse.of(categoryService.getCategoriesByStore(storeId)));
    }

    @GetMapping("/store/{storeId}/root")
    @Operation(summary = "Get root categories (no parent) for a store")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getRootCategories(
            @PathVariable UUID storeId) {
        return ResponseEntity.ok(ApiResponse.of(categoryService.getRootCategories(storeId)));
    }

    @GetMapping("/{id}/children")
    @Operation(summary = "Get child categories of a given category")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getChildCategories(
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.of(categoryService.getChildCategories(id)));
    }
}
