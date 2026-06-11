package org.eharu.shop.user.application;

import org.eharu.shop.shared.exception.ResourceNotFoundException;
import org.eharu.shop.user.domain.User;
import org.eharu.shop.user.domain.UserRepository;
import org.eharu.shop.user.domain.UserRole;
import org.eharu.shop.user.dto.CreateUserRequest;
import org.eharu.shop.user.dto.UpdateUserRequest;
import org.eharu.shop.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User buildUser(UUID id, String email, String username) {
        return User.builder()
                .id(id)
                .email(email)
                .username(username)
                .passwordHash("hashed-password")
                .role(UserRole.CUSTOMER)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createUser_success() {
        CreateUserRequest request = new CreateUserRequest(
                "alice@example.com", "alice", "password123", null);

        UUID generatedId = UUID.randomUUID();
        User savedUser = buildUser(generatedId, "alice@example.com", "alice");

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.createUser(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User captured = captor.getValue();
        assertThat(captured.getEmail()).isEqualTo("alice@example.com");
        assertThat(captured.getUsername()).isEqualTo("alice");
        assertThat(captured.getRole()).isEqualTo(UserRole.CUSTOMER);

        assertThat(response.id()).isEqualTo(generatedId);
        assertThat(response.email()).isEqualTo("alice@example.com");
    }

    @Test
    void createUser_duplicateEmail_throwsIllegalArgument() {
        CreateUserRequest request = new CreateUserRequest(
                "dup@example.com", "someuser", "password123", null);

        when(userRepository.existsByEmail("dup@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already in use");
    }

    @Test
    void getUserById_notFound_throwsResourceNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id");
    }

    @Test
    void updateUser_success() {
        UUID id = UUID.randomUUID();
        User existing = buildUser(id, "bob@example.com", "bob");
        UpdateUserRequest request = new UpdateUserRequest("bob_updated", null);

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserResponse response = userService.updateUser(id, request);

        assertThat(response.username()).isEqualTo("bob_updated");
        verify(userRepository).save(existing);
    }

    @Test
    void createUser_duplicateUsername_throwsIllegalArgument() {
        CreateUserRequest request = new CreateUserRequest(
                "new@example.com", "takenUser", "password123", null);

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("takenUser")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username already in use");
    }

    @Test
    void createUser_withExplicitRole_usesThatRole() {
        CreateUserRequest request = new CreateUserRequest(
                "merchant@example.com", "merchant1", "password123", UserRole.MERCHANT);

        UUID id = UUID.randomUUID();
        User saved = buildUser(id, "merchant@example.com", "merchant1");
        saved.setRole(UserRole.MERCHANT);

        when(userRepository.existsByEmail("merchant@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("merchant1")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserResponse response = userService.createUser(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getRole()).isEqualTo(UserRole.MERCHANT);
    }

    @Test
    void updateUser_onlyIsActive_skipsNullUsername() {
        UUID id = UUID.randomUUID();
        User existing = buildUser(id, "dave@example.com", "dave");
        UpdateUserRequest request = new UpdateUserRequest(null, false);

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserResponse response = userService.updateUser(id, request);

        assertThat(response.username()).isEqualTo("dave");
        assertThat(response.isActive()).isFalse();
    }

    @Test
    void deleteUser_success() {
        UUID id = UUID.randomUUID();
        User existing = buildUser(id, "charlie@example.com", "charlie");

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));

        userService.deleteUser(id);

        verify(userRepository).delete(existing);
    }
}
