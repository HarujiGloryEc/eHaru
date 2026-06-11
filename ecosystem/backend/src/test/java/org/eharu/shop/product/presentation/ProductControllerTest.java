package org.eharu.shop.product.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eharu.shop.product.application.ProductService;
import org.eharu.shop.product.domain.ProductStatus;
import org.eharu.shop.product.dto.CreateProductRequest;
import org.eharu.shop.product.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private ProductResponse sampleResponse(UUID id, UUID storeId) {
        return new ProductResponse(
                id,
                storeId,
                null,
                "Test Product",
                "test-product",
                null,
                null,
                new BigDecimal("19.99"),
                null,
                null,
                10,
                5,
                true,
                ProductStatus.DRAFT,
                null,
                LocalDateTime.now()
        );
    }

    @Test
    void getProductById_returns200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        when(productService.getProductById(id)).thenReturn(sampleResponse(id, storeId));

        mockMvc.perform(get("/api/v1/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Test Product"))
                .andExpect(jsonPath("$.data.slug").value("test-product"));
    }

    @Test
    void createProduct_returns201() throws Exception {
        UUID storeId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        CreateProductRequest request = new CreateProductRequest(
                storeId,
                null,
                "Test Product",
                "test-product",
                null,
                null,
                new BigDecimal("19.99"),
                null,
                null,
                0,
                5,
                true,
                null,
                null
        );

        when(productService.createProduct(any(CreateProductRequest.class)))
                .thenReturn(sampleResponse(productId, storeId));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Test Product"));
    }

    @Test
    void createProduct_missingStoreId_returns400() throws Exception {
        String body = """
                {
                    "name": "Test Product",
                    "slug": "test-product",
                    "price": 19.99
                }
                """;

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProduct_missingPrice_returns400() throws Exception {
        UUID storeId = UUID.randomUUID();
        String body = String.format("""
                {
                    "storeId": "%s",
                    "name": "Test Product",
                    "slug": "test-product"
                }
                """, storeId);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteProduct_returns204() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(productService).deleteProduct(eq(id));

        mockMvc.perform(delete("/api/v1/products/{id}", id))
                .andExpect(status().isNoContent());
    }
}
