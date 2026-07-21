package com.govtech.application.api.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.govtech.application.domain.model.ApplicationStatus;
import com.govtech.application.domain.model.DocumentType;

import lombok.Builder;

@Builder
public record ApplicationDto(
                UUID id,
                String subject,
                String aidCode,
                String aidName,
                ApplicationStatus status,
                Instant createdAt,
                List<DocumentType> missingDocuments

) {
}