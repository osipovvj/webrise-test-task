package com.github.osipovvj.webrise_test_task.controller;

import com.github.osipovvj.webrise_test_task.dto.request.ChangeSubscriptionStatusRequest;
import com.github.osipovvj.webrise_test_task.dto.request.UserRequest;
import com.github.osipovvj.webrise_test_task.dto.request.UserSubscriptionRequest;
import com.github.osipovvj.webrise_test_task.dto.response.UserResponse;
import com.github.osipovvj.webrise_test_task.dto.response.UserSubscriptionResponse;
import com.github.osipovvj.webrise_test_task.dto.response.UserSubscriptionsResponse;
import com.github.osipovvj.webrise_test_task.exception.dto.ProblemDetailResponse;
import com.github.osipovvj.webrise_test_task.service.UserService;
import com.github.osipovvj.webrise_test_task.service.UserSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "Методы управления пользователем и его подписками.")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserSubscriptionService userSubscriptionService;

    @Operation(summary = "Метод регистрации нового пользователя.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно зарегистрирован.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный формат данных.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetailResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Пользователь с указанными данными уже существует.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetailResponse.class)
                    )
            )
    })
    @PostMapping(consumes = "application/json", produces = {"application/json", "application/problem+json"})
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest request
    ) {
        log.info("Запрос на создание пользователя: {}", request);
        try {
            UserResponse response = userService.createUser(request);
            log.info("Пользователь создан: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ошибка при создании пользователя: {}", request, e);
            throw e;
        }
    }

    @Operation(summary = "Метод получения информации о пользователе.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Информация о пользователе успешно получена.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetailResponse.class)
                    )
            )
    })
    @GetMapping(value = "/{id}", produces = {"application/json", "application/problem+json"})
    public ResponseEntity<UserResponse> getUser(
            @Parameter(description = "ID пользователя.", example = "123")
            @PathVariable long id
    ) {
        log.info("Запрос на получение пользователя с id={}", id);
        try {
            UserResponse response = userService.getUserById(id);
            log.info("Получен пользователь: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ошибка при получении пользователя с id={}", id, e);
            throw e;
        }
    }

    @Operation(summary = "Метод изменения данных пользователя.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные пользователя успешно изменены.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный формат данных.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetailResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Пользователь с указанными данными уже существует.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetailResponse.class)
                    )
            )
    })
    @PutMapping(value = "/{id}", consumes = "application/json", produces = {"application/json", "application/problem+json"})
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "ID пользователя.", example = "123")
            @PathVariable long id,

            @Valid @RequestBody UserRequest userRequest
    ) {
        log.info("Запрос на обновление пользователя id={}, данные: {}", id, userRequest);
        try {
            UserResponse response = userService.updateUser(id, userRequest);
            log.info("Пользователь обновлён: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя id={}, данные: {}", id, userRequest, e);
            throw e;
        }
    }

    @Operation(summary = "Метод удаления пользователя.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Пользователь успешно удалён.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetailResponse.class)
                    )
            )
    })
    @DeleteMapping(value = "/{id}", produces = "application/problem+json")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя.", example = "123")
            @PathVariable long id
    ) {
        log.info("Запрос на удаление пользователя id={}", id);
        try {
            userService.deleteUserById(id);
            log.info("Пользователь id={} удалён", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Ошибка при удалении пользователя id={}", id, e);
            throw e;
        }
    }

    @Operation(summary = "Метод добавления новой подписки пользователю.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Подписка успешно добавлена.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserSubscriptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Несуществующая подписка.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetailResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "У пользователя уже есть эта подписка.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetailResponse.class)
                    )
            )
    })
    @PostMapping(value = "/{id}/subscriptions", consumes = "application/json", produces = {"application/json", "application/problem+json"})
    public ResponseEntity<UserSubscriptionResponse> addSubscription(
            @Parameter(description = "ID ползователя.", example = "123")
            @PathVariable long id,

            @RequestBody UserSubscriptionRequest request
    ) {
        log.info("Запрос на добавление подписки пользователю id={}, данные: {}", id, request);
        try {
            UserSubscriptionResponse response = userSubscriptionService.addUserSubscription(id, request);
            log.info("Подписка добавлена пользователю id={}: {}", id, response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ошибка при добавлении подписки пользователю id={}, данные: {}", id, request, e);
            throw e;
        }
    }

    @Operation(summary = "Метод получения списка пользовательских подписок.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список пользовательских подписок успешно получен.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserSubscriptionsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetailResponse.class)
                    )
            )
    })
    @GetMapping(value = "/{id}/subscriptions", produces = {"application/json", "application/problem+json"})
    public ResponseEntity<UserSubscriptionsResponse> getUsersSubscriptions(
            @Parameter(description = "ID пользователя.", example = "123")
            @PathVariable long id
    ) {
        log.info("Запрос на получение подписок пользователя id={}", id);
        try {
            UserSubscriptionsResponse response = userSubscriptionService.getUserSubscriptions(id);
            log.info("Получены подписки пользователя id={}: {}", id, response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ошибка при получении подписок пользователя id={}", id, e);
            throw e;
        }
    }

    @Operation(summary = "Метод изменения статуса подписки.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Статус подписки успешно изменён.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserSubscriptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не подписан на данный сервис.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetailResponse.class)
                    )
            )
    })
    @PatchMapping(value = "/{id}/subscription/{sub_id}", consumes = "application/json", produces = {"application/json", "application/problem+json"})
    public ResponseEntity<?> changeUserSubscriptionStatus(
            @Parameter(description = "ID пользователя.", example = "123")
            @PathVariable Long id,

            @Parameter(description = "ID сервиса.", example = "12")
            @PathVariable Long sub_id,

            @RequestBody ChangeSubscriptionStatusRequest request
    ) {
        log.info("Запрос на изменение статуса подписки: userId={}, subscriptionId={}, данные: {}", id, sub_id, request);
        try {
            UserSubscriptionResponse response = userSubscriptionService.changeUserSubscriptionStatus(id, sub_id, request);
            log.info("Статус подписки изменён: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ошибка при изменении статуса подписки: userId={}, subscriptionId={}, данные: {}", id, sub_id, request, e);
            throw e;
        }
    }

    @Operation(summary = "Метод удаления подписки пользователя.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Подписка успешно удалена.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не подписан на данный сервис.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetailResponse.class)
                    )
            )
    })
    @DeleteMapping(value = "/{id}/subscriptions/{sub_id}", produces = "application/problem+json")
    public ResponseEntity<Void> removeSubscription(
            @Parameter(description = "ID пользователя.", example = "123")
            @PathVariable long id,

            @Parameter(description = "ID сервиса.", example = "12")
            @PathVariable long sub_id
    ) {
        log.info("Запрос на удаление подписки: userId={}, subscriptionId={}", id, sub_id);
        try {
            userSubscriptionService.removeUserSubscription(id, sub_id);
            log.info("Подписка удалена: userId={}, subscriptionId={}", id, sub_id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Ошибка при удалении подписки: userId={}, subscriptionId={}", id, sub_id, e);
            throw e;
        }
    }
}
