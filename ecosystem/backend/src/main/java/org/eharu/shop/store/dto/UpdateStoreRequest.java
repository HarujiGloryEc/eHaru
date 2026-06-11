package org.eharu.shop.store.dto;

import org.eharu.shop.store.domain.StoreStatus;

public record UpdateStoreRequest(
        String name,
        String description,
        StoreStatus status
) {}
