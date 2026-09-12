package com.govtech.documentextra.application.command;

import com.govtech.shared.model.DocumentOrigin;
import com.govtech.shared.model.DocumentType;

import lombok.Builder;

@Builder
public record DocumentExtractionCommand(
        Long documentId,
        String applicationId,
        String bucket,
        String objectKey,
        String contentType,
        DocumentType documentType,
        DocumentOrigin origin,
        String source,
        String sha256,
        String fileName,
        Long fileSize,
        String subject,
        String model,
        String territoryCode) {
}