package org.eharu.shop.user.application;

import org.eharu.shop.user.dto.CreateUserRequest;
import org.eharu.shop.user.dto.UpdateUserRequest;
import org.eharu.shop.user.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse getUserById(UUID id);
    UserResponse getUserByEmail(String email);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(UUID id, UpdateUserRequest request);
    void deleteUser(UUID id);
}
