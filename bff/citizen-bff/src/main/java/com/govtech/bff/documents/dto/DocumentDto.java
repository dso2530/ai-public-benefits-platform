package com.govtech.bff.documents.dto;

import java.time.Instant;

public record DocumentDto(
        Long id,
        String name,
        String documentType,
        String fileName,
        Long fileSize,
        Instant uploadedAt
) {
}