package com.github.osipovvj.webrise_test_task.service;

import com.github.osipovvj.webrise_test_task.dto.request.UserRequest;
import com.github.osipovvj.webrise_test_task.dto.response.UserResponse;
import com.github.osipovvj.webrise_test_task.entity.User;
import com.github.osipovvj.webrise_test_task.exception.AlreadyExistsException;
import com.github.osipovvj.webrise_test_task.exception.ResourceNotFoundException;
import com.github.osipovvj.webrise_test_task.repository.UserRepository;
import com.github.osipovvj.webrise_test_task.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void createUser_success() {
        UserRequest request = new UserRequest("User_1", "user1@example.com");
        User user = User.builder()
                .id(1L)
                .username("User_1")
                .email("user1@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.existsByUsername("User_1")).thenReturn(false);
        when(userRepository.existsByEmail("user1@example.com")).thenReturn(false);
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        UserResponse response = userService.createUser(request);

        assertThat(response.username()).isEqualTo("User_1");
        assertThat(response.email()).isEqualTo("user1@example.com");
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void createUser_alreadyExistsByUsername() {
        UserRequest request = new UserRequest("User_1", "user1@example.com");
        when(userRepository.existsByUsername("User_1")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Пользователь с именем User_1 уже существует.");
    }

    @Test
    void createUser_alreadyExistsByEmail() {
        UserRequest request = new UserRequest("User_1", "user1@example.com");
        when(userRepository.existsByUsername("User_1")).thenReturn(false);
        when(userRepository.existsByEmail("user1@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Пользователь с email user1@example.com уже существует.");
    }

    @Test
    void getUserById_success() {
        User user = User.builder()
                .id(2L)
                .username("User_2")
                .email("user2@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(2L);

        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.username()).isEqualTo("User_2");
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Пользователь с id 99 не найден.");
    }

    @Test
    void updateUser_success_middleValue() {
        Long id = 10L;
        User existing = User.builder()
                .id(id)
                .username("OldName")
                .email("old@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserRequest request = new UserRequest("NewName", "new@example.com");
        User updated = User.builder()
                .id(id)
                .username("NewName")
                .email("new@example.com")
                .createdAt(existing.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.existsByUsernameAndIdNot("NewName", id)).thenReturn(false);
        when(userRepository.existsByEmailAndIdNot("new@example.com", id)).thenReturn(false);
        when(userRepository.save(existing)).thenReturn(updated);

        UserResponse response = userService.updateUser(id, request);

        assertThat(response.username()).isEqualTo("NewName");
        assertThat(response.email()).isEqualTo("new@example.com");
    }

    @Test
    void updateUser_usernameAlreadyExists() {
        Long id = 1L;
        User existing = User.builder()
                .id(id)
                .username("OldName")
                .email("old@example.com")
                .build();
        UserRequest request = new UserRequest("OtherName", "old@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.existsByUsernameAndIdNot("OtherName", id)).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(id, request))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Пользователь с именем OtherName уже существует.");
    }

    @Test
    void updateUser_emailAlreadyExists() {
        Long id = 1L;
        User existing = User.builder()
                .id(id)
                .username("OldName")
                .email("old@example.com")
                .build();
        UserRequest request = new UserRequest("OldName", "other@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmailAndIdNot("other@example.com", id)).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(id, request))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Пользователь с email other@example.com уже существует.");
    }

    @Test
    void updateUser_notFound() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());
        UserRequest request = new UserRequest("Any", "any@example.com");

        assertThatThrownBy(() -> userService.updateUser(100L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Пользователь с id 100 не найден.");
    }

    @Test
    void deleteUserById_success() {
        Long id = 5L;
        User user = User.builder().id(id).username("User5").email("user5@example.com").build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.deleteUserById(id);

        verify(userRepository).delete(user);
    }

    @Test
    void deleteUserById_notFound() {
        when(userRepository.findById(123L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUserById(123L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Пользователь с id 123 не найден.");
    }
}
