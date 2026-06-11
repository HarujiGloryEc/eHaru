package org.eharu.shop.store.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.eharu.shop.shared.response.ApiResponse;
import org.eharu.shop.store.application.StoreService;
import org.eharu.shop.store.dto.CreateStoreRequest;
import org.eharu.shop.store.dto.StoreResponse;
import org.eharu.shop.store.dto.UpdateStoreRequest;
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
@RequestMapping("/api/v1/stores")
@Tag(name = "Stores", description = "Store management endpoints")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    @Operation(summary = "Get all stores")
    public ResponseEntity<ApiResponse<List<StoreResponse>>> getAllStores() {
        return ResponseEntity.ok(ApiResponse.of(storeService.getAllStores()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get store by ID")
    public ResponseEntity<ApiResponse<StoreResponse>> getStoreById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.of(storeService.getStoreById(id)));
    }

    @PostMapping
    @Operation(summary = "Create a new store")
    public ResponseEntity<ApiResponse<StoreResponse>> createStore(@Valid @RequestBody CreateStoreRequest request) {
        StoreResponse created = storeService.createStore(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing store")
    public ResponseEntity<ApiResponse<StoreResponse>> updateStore(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStoreRequest request) {
        return ResponseEntity.ok(ApiResponse.of(storeService.updateStore(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a store")
    public ResponseEntity<Void> deleteStore(@PathVariable UUID id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/merchant/{merchantId}")
    @Operation(summary = "Get all stores for a merchant")
    public ResponseEntity<ApiResponse<List<StoreResponse>>> getStoresByMerchant(@PathVariable UUID merchantId) {
        return ResponseEntity.ok(ApiResponse.of(storeService.getStoresByMerchant(merchantId)));
    }
}
