package com.govtech.profile.application.dto;

import lombok.Builder;

@Builder
public record UpdateSupportingDocumentProfileCommand(
        UpdateProfileCommand profile,
        String documentId,
        String bucket,
        String objectKey,
        String contentType,
        String fileName,
        String correlationId,
        String causationId) {
}