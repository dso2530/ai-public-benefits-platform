package com.govtech.platform.web.error;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiError> handleApplicationException(
            ApplicationException ex,
            HttpServletRequest request) {

        HttpStatus status = resolveStatus(ex.getCode());

        return ResponseEntity
                .status(status)
                .body(buildError(
                        status,
                        ex.getCode(),
                        ex.getMessage(),
                        request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField()
                        + ": "
                        + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .badRequest()
                .body(buildError(
                        HttpStatus.BAD_REQUEST,
                        ErrorCode.VALIDATION_ERROR,
                        message,
                        request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(
            Exception ex,
            HttpServletRequest request) {

        log.error(
                "Unhandled exception on {} {}",
                request.getMethod(),
                request.getRequestURI(),
                ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ErrorCode.INTERNAL_SERVER_ERROR,
                        "Une erreur interne est survenue.",
                        request));
    }

    private ApiError buildError(
            HttpStatus status,
            ErrorCode code,
            String message,
            HttpServletRequest request) {

        return new ApiError(
                Instant.now(),
                status.value(),
                code.name(),
                message,
                request.getRequestURI(),
                request.getHeader("X-Correlation-Id"));
    }

    private HttpStatus resolveStatus(ErrorCode code) {

        return switch (code) {

            case RESOURCE_NOT_FOUND ->
                HttpStatus.NOT_FOUND;

            case RESOURCE_CONFLICT ->
                HttpStatus.CONFLICT;

            case UNAUTHORIZED ->
                HttpStatus.UNAUTHORIZED;

            case FORBIDDEN ->
                HttpStatus.FORBIDDEN;

            case VALIDATION_ERROR,
                    BAD_REQUEST ->
                HttpStatus.BAD_REQUEST;

            case EXTERNAL_SERVICE_UNAVAILABLE ->
                HttpStatus.SERVICE_UNAVAILABLE;

            case EXTERNAL_SERVICE_ERROR ->
                HttpStatus.BAD_GATEWAY;

            default ->
                HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}