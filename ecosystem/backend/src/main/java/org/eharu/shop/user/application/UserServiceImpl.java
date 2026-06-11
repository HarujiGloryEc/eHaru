package org.eharu.shop.user.application;

import org.eharu.shop.shared.exception.ResourceNotFoundException;
import org.eharu.shop.user.domain.User;
import org.eharu.shop.user.domain.UserRepository;
import org.eharu.shop.user.domain.UserRole;
import org.eharu.shop.user.dto.CreateUserRequest;
import org.eharu.shop.user.dto.UpdateUserRequest;
import org.eharu.shop.user.dto.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use: " + request.email());
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already in use: " + request.username());
        }

        UserRole role = request.role() != null ? request.role() : UserRole.CUSTOMER;

        User user = User.builder()
                .email(request.email())
                .username(request.username())
                .passwordHash(request.password())
                .role(role)
                .isActive(true)
                .build();

        return UserResponse.from(userRepository.save(user));
    }

    @Override
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
        return UserResponse.from(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return UserResponse.from(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    @Override
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));

        if (request.username() != null) {
            user.setUsername(request.username());
        }
        if (request.isActive() != null) {
            user.setActive(request.isActive());
        }

        return UserResponse.from(userRepository.save(user));
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
        userRepository.delete(user);
    }
}
