package com.govtech.bff.application.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ApplicationDto(
    UUID id,
    String subject,
    String aidCode,
    String aidName,
    ApplicationStatus status,
    Instant createdAt,
    List<DocumentType> missingDocuments) {}
