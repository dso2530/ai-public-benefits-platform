package com.govtech.security.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityScanJpaRepository
                extends JpaRepository<SecurityScanJpaEntity, UUID> {

        Optional<SecurityScanJpaEntity> findFirstByDocumentIdOrderByScannedAtDesc(
                        Long documentId);

        Optional<SecurityScanJpaEntity> findByDocumentIdAndSha256(
                        Long documentId,
                        String sha256);

}