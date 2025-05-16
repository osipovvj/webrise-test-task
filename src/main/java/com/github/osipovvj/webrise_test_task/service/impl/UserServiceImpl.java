package com.github.osipovvj.webrise_test_task.service.impl;

import com.github.osipovvj.webrise_test_task.dto.request.UserRequest;
import com.github.osipovvj.webrise_test_task.dto.response.UserResponse;
import com.github.osipovvj.webrise_test_task.entity.User;
import com.github.osipovvj.webrise_test_task.exception.ResourceNotFoundException;
import com.github.osipovvj.webrise_test_task.exception.AlreadyExistsException;
import com.github.osipovvj.webrise_test_task.repository.UserRepository;
import com.github.osipovvj.webrise_test_task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new AlreadyExistsException("Пользователь с именем " + request.username() + " уже существует.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new AlreadyExistsException("Пользователь с email " + request.email() + " уже существует.");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .build();

        return UserResponse.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден."));

        return UserResponse.toResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден."));
        if (!user.getUsername().equals(request.username())) {
            if (userRepository.existsByUsernameAndIdNot(request.username(), id)) {
                throw new AlreadyExistsException("Пользователь с именем " + request.username() + " уже существует.");
            }
        }
        if (!user.getEmail().equals(request.email())) {
            if (userRepository.existsByEmailAndIdNot(request.email(), id)) {
                throw new AlreadyExistsException("Пользователь с email " + request.email() + " уже существует.");
            }
        }

        user.setUsername(request.username());
        user.setEmail(request.email());

        return UserResponse.toResponse(userRepository.save(user));
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден."));

        userRepository.delete(user);
    }
}
