package com.govtech.profile.application.dto;

import com.govtech.shared.model.DocumentOrigin;
import com.govtech.shared.model.DocumentType;

import lombok.Builder;

@Builder
public record DocumentCommand(
        Long documentId,
        String bucket,
        String objectKey,
        String contentType,
        String fileName,
        Long fileSize,
        String sha256,
        DocumentType documentType,
        DocumentOrigin origin) {
}