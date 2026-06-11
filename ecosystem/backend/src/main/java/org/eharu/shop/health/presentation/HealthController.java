package org.eharu.shop.health.presentation;

import org.eharu.shop.health.application.HealthService;
import org.eharu.shop.health.domain.HealthStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Health", description = "Service health check")
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping("/health")
    @Operation(summary = "Get service health status")
    public ResponseEntity<HealthStatus> getHealth() {
        return ResponseEntity.ok(healthService.getStatus());
    }
}
