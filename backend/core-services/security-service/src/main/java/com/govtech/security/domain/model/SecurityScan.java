package com.govtech.security.domain.model;

import java.time.Instant;
import java.util.UUID;

import com.govtech.shared.model.DocumentOrigin;
import com.govtech.shared.model.DocumentType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SecurityScan {

    private UUID id;

    private Long documentId;

    private String sha256;

    private SecurityStatus status;

    private String scanEngine;

    private Instant scannedAt;

    private DocumentType documentType;

    private String detectedContentType;

    private DocumentOrigin origin;

    private String bucket;

    private String objectKey;

    private String contentType;

    private String fileName;

    public boolean isClean() {
        return status == SecurityStatus.CLEAN;
    }
}
