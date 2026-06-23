package com.govtech.document.application.dto;
import java.time.Instant;

public record DocumentDto(
        Long id,
        String name,
        String documentType,
        String status,
        String fileName,
        Long fileSize,
        Instant uploadedAt
) {}