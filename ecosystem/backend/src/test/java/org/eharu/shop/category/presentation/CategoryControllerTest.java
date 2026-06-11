package org.eharu.shop.category.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eharu.shop.category.application.CategoryService;
import org.eharu.shop.category.dto.CategoryResponse;
import org.eharu.shop.category.dto.CreateCategoryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    void getCategoryById_returns200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        CategoryResponse response = new CategoryResponse(
                id, "Electronics", "electronics", null, 0, true, storeId, null, LocalDateTime.now());

        when(categoryService.getCategoryById(id)).thenReturn(response);

        mockMvc.perform(get("/api/v1/categories/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Electronics"))
                .andExpect(jsonPath("$.data.slug").value("electronics"));
    }

    @Test
    void createCategory_returns201() throws Exception {
        UUID storeId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        CreateCategoryRequest request = new CreateCategoryRequest(
                storeId, null, "Electronics", "electronics", null, 0);
        CategoryResponse response = new CategoryResponse(
                id, "Electronics", "electronics", null, 0, true, storeId, null, LocalDateTime.now());

        when(categoryService.createCategory(any(CreateCategoryRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/categories/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.slug").value("electronics"));
    }

    @Test
    void createCategory_missingStoreId_returns400() throws Exception {
        String bodyMissingStoreId = """
                {"name":"Electronics","slug":"electronics","displayOrder":0}
                """;

        mockMvc.perform(post("/api/v1/categories/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyMissingStoreId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCategory_returns204() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(categoryService).deleteCategory(id);

        mockMvc.perform(delete("/api/v1/categories/{id}", id))
                .andExpect(status().isNoContent());
    }
}
