package com.govtech.application.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder
public record Application(
                UUID applicationId,
                String subject,
                String aidCode,
                String aidName,
                ApplicationStatus status,
                String objectKey,
                Instant createdAt,
                List<DocumentType> missingDocuments) {
}