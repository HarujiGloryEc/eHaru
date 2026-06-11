package org.eharu.shop.health.application;

import org.eharu.shop.health.domain.HealthStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HealthServiceTest {

    private HealthService healthService;

    @BeforeEach
    void setUp() {
        healthService = new HealthService();
    }

    @Test
    void getStatus_returnsUpStatus() {
        HealthStatus status = healthService.getStatus();

        assertThat(status).isNotNull();
        assertThat(status.status()).isEqualTo("UP");
        assertThat(status.service()).isEqualTo("haru-shop-api");
    }
}
