package com.govtech.mock.dgfip;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class DgfipController {

  private final AtomicInteger retryCounter = new AtomicInteger();

  @GetMapping("/income/{id}")
  public ResponseEntity<?> income(@PathVariable String id) throws InterruptedException {

    switch (id) {
      case "401":
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(
                Map.of(
                    "error", "invalid_token",
                    "message", "Invalid OAuth2 token"));

      case "403":
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(
                Map.of(
                    "error", "access_denied",
                    "message", "Access denied"));

      case "404":
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("message", "Citizen not found"));

      case "429":
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
            .body(Map.of("message", "Too many requests"));

      case "500":
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("message", "Internal server error"));

      case "TIMEOUT":
        Thread.sleep(10_000);
        break;

      case "INVALID_JSON":
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                                {
                                  "annualIncome":
                                """);
      case "RETRY":
        if (retryCounter.incrementAndGet() < 3) {
          return ResponseEntity.internalServerError().body(Map.of("message", "Temporary error"));
        }
        retryCounter.set(0);
        break;
    }

    return ResponseEntity.ok(
        Map.of(
            "fiscalNumber",
            id,
            "annualIncome",
            new BigDecimal("28500.00"),
            "taxableIncome",
            new BigDecimal("26200.00"),
            "fiscalYear",
            "2025"));
  }

  @GetMapping("/tax-notices/latest/{id}")
  public ResponseEntity<?> latestTaxNotice(@PathVariable String id) throws InterruptedException {

    switch (id) {
      case "401":
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(
                Map.of(
                    "error", "invalid_token",
                    "message", "Invalid OAuth2 token"));

      case "403":
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(
                Map.of(
                    "error", "access_denied",
                    "message", "Access denied"));

      case "404":
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("message", "Tax notice not found"));

      case "429":
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
            .body(Map.of("message", "Too many requests"));

      case "500":
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("message", "Internal server error"));

      case "TIMEOUT":
        Thread.sleep(10_000);
        break;

      case "INVALID_JSON":
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                                {
                                  "referenceNumber":
                                """);

      case "RETRY":
        if (retryCounter.incrementAndGet() < 3) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(Map.of("message", "Temporary error"));
        }
        retryCounter.set(0);
        break;
    }

    return ResponseEntity.ok(
        Map.of(
            "referenceNumber",
            "AV-2025-000123",
            "fiscalYear",
            "2025",
            "taxableIncome",
            new BigDecimal("26200.00"),
            "taxAmount",
            new BigDecimal("1200.00")));
  }
}
