package com.govtech.security.domain.port;

import java.util.Optional;

import com.govtech.security.domain.model.SecurityScan;

public interface SecurityScanRepositoryPort {

    Optional<SecurityScan> findLatestByDocumentId(Long documentId);

    SecurityScan save(
            SecurityScan scan);

}