package com.govtech.application.service.client.dto;

import java.time.Instant;

import com.govtech.application.domain.model.DocumentType;

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
                String bucket,
                String objectKey,
                Long fileSize,
                Instant uploadedAt) {
}