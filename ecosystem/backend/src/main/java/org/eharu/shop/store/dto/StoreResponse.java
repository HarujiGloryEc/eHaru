package org.eharu.shop.store.dto;

import org.eharu.shop.store.domain.Store;
import org.eharu.shop.store.domain.StoreStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record StoreResponse(
        UUID id,
        String name,
        String slug,
        String description,
        StoreStatus status,
        UUID merchantId,
        LocalDateTime createdAt
) {
    public static StoreResponse from(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getSlug(),
                store.getDescription(),
                store.getStatus(),
                store.getMerchant().getId(),
                store.getCreatedAt()
        );
    }
}
