package com.govtech.extraction.domain.model;

public record Document(

                Long documentId,
                String subject,
                String bucket,
                String objectKey,
                String contentType,
                DocumentType type,
                String text

) {
}