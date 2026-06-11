package org.eharu.shop.health.application;

import org.eharu.shop.health.domain.HealthStatus;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

    public HealthStatus getStatus() {
        return new HealthStatus("UP", "haru-shop-api");
    }
}
