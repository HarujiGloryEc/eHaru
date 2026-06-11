package org.eharu.shop.store.application;

import org.eharu.shop.store.dto.CreateStoreRequest;
import org.eharu.shop.store.dto.StoreResponse;
import org.eharu.shop.store.dto.UpdateStoreRequest;

import java.util.List;
import java.util.UUID;

public interface StoreService {
    StoreResponse createStore(CreateStoreRequest request);
    StoreResponse getStoreById(UUID id);
    StoreResponse getStoreBySlug(String slug);
    List<StoreResponse> getStoresByMerchant(UUID merchantId);
    List<StoreResponse> getAllStores();
    StoreResponse updateStore(UUID id, UpdateStoreRequest request);
    void deleteStore(UUID id);
}
