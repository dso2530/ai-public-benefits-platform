package com.govtech.apply.application.dto;

import com.govtech.shared.model.DocumentType;

import lombok.Builder;

@Builder
public record StoredDocument(

                Long documentId,

                String name,

                DocumentType documentType,

                String status,

                String fileName,

                String contentType,

                String bucket,

                String objectKey,

                Long fileSize) {
}