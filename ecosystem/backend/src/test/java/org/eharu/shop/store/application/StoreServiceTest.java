package org.eharu.shop.store.application;

import org.eharu.shop.shared.exception.ResourceNotFoundException;
import org.eharu.shop.store.domain.Store;
import org.eharu.shop.store.domain.StoreRepository;
import org.eharu.shop.store.domain.StoreStatus;
import org.eharu.shop.store.dto.CreateStoreRequest;
import org.eharu.shop.store.dto.StoreResponse;
import org.eharu.shop.store.dto.UpdateStoreRequest;
import org.eharu.shop.user.domain.User;
import org.eharu.shop.user.domain.UserRepository;
import org.eharu.shop.user.domain.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StoreServiceImpl storeService;

    private User buildMerchant(UUID id) {
        return User.builder()
                .id(id)
                .email("merchant@example.com")
                .username("merchant")
                .passwordHash("hashed")
                .role(UserRole.MERCHANT)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Store buildStore(UUID id, User merchant) {
        return Store.builder()
                .id(id)
                .merchant(merchant)
                .name("Test Store")
                .slug("test-store")
                .description("A test store")
                .status(StoreStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createStore_success() {
        UUID merchantId = UUID.randomUUID();
        User merchant = buildMerchant(merchantId);
        CreateStoreRequest request = new CreateStoreRequest("Test Store", "test-store", "A test store", merchantId);

        UUID storeId = UUID.randomUUID();
        Store saved = buildStore(storeId, merchant);

        when(userRepository.findById(merchantId)).thenReturn(Optional.of(merchant));
        when(storeRepository.existsBySlug("test-store")).thenReturn(false);
        when(storeRepository.save(any(Store.class))).thenReturn(saved);

        StoreResponse response = storeService.createStore(request);

        ArgumentCaptor<Store> captor = ArgumentCaptor.forClass(Store.class);
        verify(storeRepository).save(captor.capture());

        Store captured = captor.getValue();
        assertThat(captured.getName()).isEqualTo("Test Store");
        assertThat(captured.getSlug()).isEqualTo("test-store");
        assertThat(captured.getMerchant()).isEqualTo(merchant);

        assertThat(response.id()).isEqualTo(storeId);
        assertThat(response.name()).isEqualTo("Test Store");
        assertThat(response.merchantId()).isEqualTo(merchantId);
    }

    @Test
    void createStore_merchantNotFound_throwsResourceNotFound() {
        UUID merchantId = UUID.randomUUID();
        CreateStoreRequest request = new CreateStoreRequest("Store", "store-slug", null, merchantId);

        when(userRepository.findById(merchantId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storeService.createStore(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id");
    }

    @Test
    void createStore_duplicateSlug_throwsIllegalArgument() {
        UUID merchantId = UUID.randomUUID();
        User merchant = buildMerchant(merchantId);
        CreateStoreRequest request = new CreateStoreRequest("Store", "existing-slug", null, merchantId);

        when(userRepository.findById(merchantId)).thenReturn(Optional.of(merchant));
        when(storeRepository.existsBySlug("existing-slug")).thenReturn(true);

        assertThatThrownBy(() -> storeService.createStore(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("existing-slug");
    }

    @Test
    void getStoreById_notFound_throwsResourceNotFound() {
        UUID id = UUID.randomUUID();
        when(storeRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storeService.getStoreById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Store not found with id");
    }

    @Test
    void updateStore_success() {
        UUID storeId = UUID.randomUUID();
        User merchant = buildMerchant(UUID.randomUUID());
        Store existing = buildStore(storeId, merchant);
        UpdateStoreRequest request = new UpdateStoreRequest("Updated Name", "Updated description", StoreStatus.INACTIVE);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(existing));
        when(storeRepository.save(any(Store.class))).thenAnswer(inv -> inv.getArgument(0));

        StoreResponse response = storeService.updateStore(storeId, request);

        assertThat(response.name()).isEqualTo("Updated Name");
        assertThat(response.description()).isEqualTo("Updated description");
        assertThat(response.status()).isEqualTo(StoreStatus.INACTIVE);
        verify(storeRepository).save(existing);
    }

    @Test
    void updateStore_withAllNullFields_changesNothing() {
        UUID storeId = UUID.randomUUID();
        User merchant = buildMerchant(UUID.randomUUID());
        Store existing = buildStore(storeId, merchant);
        UpdateStoreRequest request = new UpdateStoreRequest(null, null, null);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(existing));
        when(storeRepository.save(any(Store.class))).thenAnswer(inv -> inv.getArgument(0));

        StoreResponse response = storeService.updateStore(storeId, request);

        assertThat(response.name()).isEqualTo("Test Store");
        assertThat(response.status()).isEqualTo(StoreStatus.ACTIVE);
    }

    @Test
    void deleteStore_success() {
        UUID storeId = UUID.randomUUID();
        User merchant = buildMerchant(UUID.randomUUID());
        Store existing = buildStore(storeId, merchant);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(existing));

        storeService.deleteStore(storeId);

        verify(storeRepository).delete(existing);
    }
}
