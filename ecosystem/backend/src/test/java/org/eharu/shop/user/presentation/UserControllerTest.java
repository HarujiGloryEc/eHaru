package org.eharu.shop.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eharu.shop.user.application.UserService;
import org.eharu.shop.user.domain.UserRole;
import org.eharu.shop.user.dto.CreateUserRequest;
import org.eharu.shop.user.dto.UserResponse;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserResponse sampleResponse(UUID id) {
        return new UserResponse(id, "test@example.com", "testuser",
                UserRole.CUSTOMER, true, LocalDateTime.now());
    }

    @Test
    void getAllUsers_returns200() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.getAllUsers()).thenReturn(List.of(sampleResponse(id)));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].email").value("test@example.com"));
    }

    @Test
    void getUserById_returns200() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.getUserById(id)).thenReturn(sampleResponse(id));

        mockMvc.perform(get("/api/v1/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    void createUser_returns201() throws Exception {
        UUID id = UUID.randomUUID();
        CreateUserRequest request = new CreateUserRequest(
                "new@example.com", "newuser", "password123", null);

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(sampleResponse(id));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    void createUser_invalidEmail_returns400() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "not-an-email", "newuser", "password123", null);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_returns204() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(userService).deleteUser(eq(id));

        mockMvc.perform(delete("/api/v1/users/{id}", id))
                .andExpect(status().isNoContent());
    }
}
