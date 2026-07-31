package com.govtech.security.api.dto;

import java.time.Instant;

import com.govtech.security.domain.model.SecurityScan;
import com.govtech.security.domain.model.SecurityStatus;

public record SecurityScanResponse(

        Long documentId,

        SecurityStatus status,

        String scanEngine,

        Instant scannedAt

) {

    public static SecurityScanResponse from(
            SecurityScan scan) {

        return new SecurityScanResponse(

                scan.getDocumentId(),

                scan.getStatus(),

                scan.getScanEngine(),

                scan.getScannedAt());
    }

}