package com.govtech.eligibility.api.advice;

import com.govtech.eligibility.domain.exception.EligibilityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EligibilityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleEligibilityException(EligibilityException ex) {

        log.error("Eligibility error", ex);

        return Map.of(
                "timestamp", Instant.now(),
                "error", "ELIGIBILITY_ERROR",
                "message", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleException(Exception ex) {

        log.error("Unexpected error", ex);

        return Map.of(
                "timestamp", Instant.now(),
                "error", "INTERNAL_SERVER_ERROR",
                "message", "Unexpected error");
    }
}