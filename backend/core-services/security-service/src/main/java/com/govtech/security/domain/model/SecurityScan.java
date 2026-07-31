package com.govtech.security.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class SecurityScan {

    private UUID id;

    private Long documentId;

    private String sha256;

    private SecurityStatus status;

    private String scanEngine;

    private Instant scannedAt;

    private String detectedContentType;

    public boolean isClean() {

        return status == SecurityStatus.CLEAN;
    }

}