package com.govtech.document.api.dto;

import java.time.Instant;

import com.govtech.shared.model.DocumentType;

import lombok.Builder;

@Builder
public record DocumentContractDto(

                Long documentId,
                String subject,
                String name,
                DocumentType documentType,
                String status,
                String fileName,
                String contentType,
                String objectKey,
                Long fileSize,
                Instant uploadedAt) {
}