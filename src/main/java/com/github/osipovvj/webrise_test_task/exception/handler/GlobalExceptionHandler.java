package com.github.osipovvj.webrise_test_task.exception.handler;

import com.github.osipovvj.webrise_test_task.exception.ResourceNotFoundException;
import com.github.osipovvj.webrise_test_task.exception.AlreadyExistsException;
import com.github.osipovvj.webrise_test_task.exception.dto.ProblemDetailResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetailResponse handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<ProblemDetailResponse.FieldErrorDetail> errors = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ProblemDetailResponse.FieldErrorDetail(
                        fieldError.getField(), fieldError.getDefaultMessage()
                ))
                .toList();

        return new ProblemDetailResponse(
                LocalDateTime.now(),
                "/errors/not-valid",
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                "Одно или несколько полей содержат ошибки.",
                request.getRequestURI(),
                errors
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetailResponse handleNotFoundException(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {
        return new ProblemDetailResponse(
                LocalDateTime.now(),
                "/error/not-found",
                "Not Found Error",
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetailResponse handleAlreadyExistsException(
            AlreadyExistsException exception,
            HttpServletRequest request
    ) {
        return new ProblemDetailResponse(
                LocalDateTime.now(),
                "/error/already-exists",
                "Already Exists Error",
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                request.getRequestURI(),
                null
        );
    }
}
