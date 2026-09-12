package com.govtech.document.application.command;

import com.govtech.shared.model.DocumentType;

import lombok.Builder;

@Builder
public record UpdateDocumentSecurityCommand(

        Long documentId,

        String securityStatus,

        String scanEngine,

        String scannedAt,

        String sha256,

        String detectedContentType,

        String bucket,

        String objectKey,

        String contentType,

        String fileName,

        DocumentType documentType) {
}