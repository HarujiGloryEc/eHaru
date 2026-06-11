package org.eharu.shop.store.application;

import org.eharu.shop.shared.exception.ResourceNotFoundException;
import org.eharu.shop.store.domain.Store;
import org.eharu.shop.store.domain.StoreRepository;
import org.eharu.shop.store.dto.CreateStoreRequest;
import org.eharu.shop.store.dto.StoreResponse;
import org.eharu.shop.store.dto.UpdateStoreRequest;
import org.eharu.shop.user.domain.User;
import org.eharu.shop.user.domain.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public StoreServiceImpl(StoreRepository storeRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public StoreResponse createStore(CreateStoreRequest request) {
        User merchant = userRepository.findById(request.merchantId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.merchantId()));

        if (storeRepository.existsBySlug(request.slug())) {
            throw new IllegalArgumentException("A store with slug '" + request.slug() + "' already exists");
        }

        Store store = Store.builder()
                .merchant(merchant)
                .name(request.name())
                .slug(request.slug())
                .description(request.description())
                .build();

        return StoreResponse.from(storeRepository.save(store));
    }

    @Override
    public StoreResponse getStoreById(UUID id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "id", id));
        return StoreResponse.from(store);
    }

    @Override
    public StoreResponse getStoreBySlug(String slug) {
        Store store = storeRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "slug", slug));
        return StoreResponse.from(store);
    }

    @Override
    public List<StoreResponse> getStoresByMerchant(UUID merchantId) {
        return storeRepository.findByMerchantId(merchantId).stream()
                .map(StoreResponse::from)
                .toList();
    }

    @Override
    public List<StoreResponse> getAllStores() {
        return storeRepository.findAll().stream()
                .map(StoreResponse::from)
                .toList();
    }

    @Override
    public StoreResponse updateStore(UUID id, UpdateStoreRequest request) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "id", id));

        if (request.name() != null) {
            store.setName(request.name());
        }
        if (request.description() != null) {
            store.setDescription(request.description());
        }
        if (request.status() != null) {
            store.setStatus(request.status());
        }

        return StoreResponse.from(storeRepository.save(store));
    }

    @Override
    public void deleteStore(UUID id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "id", id));
        storeRepository.delete(store);
    }
}
