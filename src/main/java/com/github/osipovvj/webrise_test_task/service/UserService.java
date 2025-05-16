package com.github.osipovvj.webrise_test_task.service;

import com.github.osipovvj.webrise_test_task.dto.request.UserRequest;
import com.github.osipovvj.webrise_test_task.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest request);
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UserRequest request);
    void deleteUserById(Long id);
}
