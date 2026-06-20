package com.govtech.bff.dashboard.dto;

import java.time.Instant;

public record DocumentDto(
        Long id,
        String name,
        String type,
        String status,
        Instant uploadedAt
) {
}