package com.govtech.document.domain.model;

import java.time.Instant;
import java.util.UUID;

import com.govtech.shared.model.DocumentOrigin;
import com.govtech.shared.model.DocumentType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Document {

    private Long id;

    private UUID applicationId;

    private String subject;

    private String bucket;

    private String objectKey;

    private String contentType;

    private DocumentType documentType;

    private DocumentOrigin origin;

    private String source;

    private String sha256;

    private String fileName;

    private Long fileSize;

    private String model;

    private Instant uploadedAt;

    private String territoryCode;
}