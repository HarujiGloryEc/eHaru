package org.eharu.shop.shared.exception;

import org.eharu.shop.health.application.HealthService;
import org.eharu.shop.health.presentation.HealthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthService healthService;

    @Test
    void resourceNotFoundException_returns404WithApiError() throws Exception {
        when(healthService.getStatus())
            .thenThrow(new ResourceNotFoundException("Item", "id", "42"));

        mockMvc.perform(get("/api/v1/health"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("NOT_FOUND"))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void illegalArgumentException_returns400WithApiError() throws Exception {
        when(healthService.getStatus())
            .thenThrow(new IllegalArgumentException("duplicate resource"));

        mockMvc.perform(get("/api/v1/health"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void genericException_returns500WithApiError() throws Exception {
        when(healthService.getStatus())
            .thenThrow(new RuntimeException("unexpected error"));

        mockMvc.perform(get("/api/v1/health"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
            .andExpect(jsonPath("$.timestamp").exists());
    }
}
