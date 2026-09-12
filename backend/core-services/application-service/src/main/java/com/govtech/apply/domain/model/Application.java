package com.govtech.apply.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.govtech.shared.model.DocumentType;

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

        public Application withStatus(ApplicationStatus status) {
                return Application.builder()
                                .applicationId(applicationId)
                                .subject(subject)
                                .aidCode(aidCode)
                                .aidName(aidName)
                                .status(status)
                                .objectKey(objectKey)
                                .createdAt(createdAt)
                                .missingDocuments(missingDocuments)
                                .build();
        }

        public Application withMissingDocuments(
                        List<DocumentType> missingDocuments) {

                return Application.builder()
                                .applicationId(applicationId)
                                .subject(subject)
                                .aidCode(aidCode)
                                .aidName(aidName)
                                .status(status)
                                .objectKey(objectKey)
                                .createdAt(createdAt)
                                .missingDocuments(missingDocuments)
                                .build();
        }
}