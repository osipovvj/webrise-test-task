package com.github.osipovvj.webrise_test_task.controller;

import com.github.osipovvj.webrise_test_task.dto.request.SubscriptionRequest;
import com.github.osipovvj.webrise_test_task.dto.response.SubscriptionResponse;
import com.github.osipovvj.webrise_test_task.dto.response.SubscriptionsResponse;
import com.github.osipovvj.webrise_test_task.dto.response.TopSubscriptionsResponse;
import com.github.osipovvj.webrise_test_task.exception.dto.ProblemDetailResponse;
import com.github.osipovvj.webrise_test_task.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
@Tag(name = "SubscriptionController", description = "Методы управления сервисами.")
public class SubscriptionController {
    private static final Logger log = LoggerFactory.getLogger(SubscriptionController.class);

    private final SubscriptionService subscriptionService;

    @Operation(summary = "Метод получения топ 3 самых популярных сервиса.")
    @ApiResponse(
            responseCode = "200",
            description = "Список успешно получен.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TopSubscriptionsResponse.class)
            )
    )
    @GetMapping(value = "/top", produces = "application/json")
    public ResponseEntity<TopSubscriptionsResponse> getTopSubscriptions() {
        log.info("Запрос на получение топ-3 популярных сервисов");
        try {
            TopSubscriptionsResponse response = subscriptionService.getTopSubscriptions();
            log.info("Топ-3 сервисов успешно получены: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ошибка при получении топ-3 сервисов", e);
            throw e;
        }
    }

    @Operation(summary = "Метод создания нового сервиса.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Сервис успешно создан.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Сервис с таким названием уже существует.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetailResponse.class)
                    )
            )
    })
    @PostMapping(consumes = "application/json", produces = {"application/json", "application/problem+json"})
    public ResponseEntity<SubscriptionResponse> createSubscription(@RequestBody SubscriptionRequest request) {
        log.info("Запрос на создание сервиса: {}", request);
        try {
            SubscriptionResponse response = subscriptionService.createSubscription(request);
            log.info("Сервис создан: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ошибка при создании сервиса: {}", request, e);
            throw e;
        }
    }

    @Operation(summary = "Метод получения списка всех сервисов.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список успешно получен.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionsResponse.class)
                    )
            )
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<SubscriptionsResponse> updateSubscription() {
        log.info("Запрос на получение всех сервисов");
        try {
            SubscriptionsResponse response = subscriptionService.getSubscriptions();
            log.info("Список сервисов успешно получен: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ошибка при получении списка сервисов", e);
            throw e;
        }
    }

    @Operation(summary = "Метод изменения информации о сервисе.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Информация о сервисе успешно изменена.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сервис не найден.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetailResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Сервис с таким именем уже существует.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetailResponse.class)
                    )
            )
    })
    @PutMapping(value = "/{id}", consumes = "application/json", produces = {"application/json", "application/problem+json"})
    public ResponseEntity<SubscriptionResponse> updateSubscription(
            @Parameter(description = "ID сервиса.", example = "20")
            @PathVariable Long id,
            @RequestBody SubscriptionRequest request
    ) {
        log.info("Запрос на обновление сервиса id={}, данные: {}", id, request);
        try {
            SubscriptionResponse response = subscriptionService.updateSubscription(id, request);
            log.info("Сервис обновлён: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ошибка при обновлении сервиса id={}, данные: {}", id, request, e);
            throw e;
        }
    }

    @Operation(summary = "Метод удаления сервиса.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Сервис успешно удалён.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сервис не найден.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetailResponse.class)
                    )
            )
    })
    @DeleteMapping(value = "/{id}", produces = "application/problem+json")
    public ResponseEntity<Void> deleteSubscription(
            @Parameter(description = "ID сервиса.", example = "20")
            @PathVariable long id
    ) {
        log.info("Запрос на удаление сервиса id={}", id);
        try {
            subscriptionService.deleteSubscription(id);
            log.info("Сервис id={} успешно удалён", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Ошибка при удалении сервиса id={}", id, e);
            throw e;
        }
    }
}
