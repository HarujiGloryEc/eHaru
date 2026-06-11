package org.eharu.shop.store.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eharu.shop.store.application.StoreService;
import org.eharu.shop.store.domain.StoreStatus;
import org.eharu.shop.store.dto.CreateStoreRequest;
import org.eharu.shop.store.dto.StoreResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreController.class)
class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StoreService storeService;

    private StoreResponse sampleResponse(UUID id, UUID merchantId) {
        return new StoreResponse(id, "Test Store", "test-store", "A test store",
                StoreStatus.ACTIVE, merchantId, LocalDateTime.now());
    }

    @Test
    void getAllStores_returns200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID merchantId = UUID.randomUUID();
        when(storeService.getAllStores()).thenReturn(List.of(sampleResponse(id, merchantId)));

        mockMvc.perform(get("/api/v1/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Test Store"));
    }

    @Test
    void getStoreById_returns200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID merchantId = UUID.randomUUID();
        when(storeService.getStoreById(id)).thenReturn(sampleResponse(id, merchantId));

        mockMvc.perform(get("/api/v1/stores/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.slug").value("test-store"));
    }

    @Test
    void createStore_returns201() throws Exception {
        UUID id = UUID.randomUUID();
        UUID merchantId = UUID.randomUUID();
        CreateStoreRequest request = new CreateStoreRequest("Test Store", "test-store", "A test store", merchantId);

        when(storeService.createStore(any(CreateStoreRequest.class))).thenReturn(sampleResponse(id, merchantId));

        mockMvc.perform(post("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Test Store"));
    }

    @Test
    void createStore_missingMerchantId_returns400() throws Exception {
        // merchantId is null — violates @NotNull constraint
        CreateStoreRequest request = new CreateStoreRequest("Test Store", "test-store", null, null);

        mockMvc.perform(post("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteStore_returns204() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(storeService).deleteStore(eq(id));

        mockMvc.perform(delete("/api/v1/stores/{id}", id))
                .andExpect(status().isNoContent());
    }
}
