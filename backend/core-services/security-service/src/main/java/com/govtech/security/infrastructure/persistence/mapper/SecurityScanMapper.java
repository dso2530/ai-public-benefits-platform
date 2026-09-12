package com.govtech.security.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.govtech.security.domain.model.SecurityScan;
import com.govtech.security.domain.model.SecurityStatus;
import com.govtech.security.infrastructure.persistence.SecurityScanJpaEntity;

@Mapper(componentModel = "spring")
public interface SecurityScanMapper {

        default SecurityScan toDomain(
                        SecurityScanJpaEntity entity) {

                if (entity == null) {
                        return null;
                }

                return SecurityScan.builder()

                                .id(entity.getId())

                                .documentId(
                                                entity.getDocumentId())

                                .sha256(
                                                entity.getSha256())

                                .status(
                                                SecurityStatus.valueOf(
                                                                entity.getStatus()))

                                .scanEngine(
                                                entity.getScanEngine())

                                .scannedAt(
                                                entity.getScannedAt())

                                .origin(
                                                entity.getOrigin())

                                .documentType(
                                                entity.getDocumentType())

                                .detectedContentType(
                                                entity.getDetectedContentType())

                                .bucket(
                                                entity.getBucket())

                                .objectKey(
                                                entity.getObjectKey())

                                .contentType(
                                                entity.getContentType())

                                .fileName(
                                                entity.getFileName())

                                .build();
        }

        default SecurityScanJpaEntity toEntity(
                        SecurityScan domain) {

                if (domain == null) {
                        return null;
                }

                return SecurityScanJpaEntity.builder()

                                .id(domain.getId())

                                .documentId(
                                                domain.getDocumentId())

                                .sha256(
                                                domain.getSha256())

                                .status(
                                                domain.getStatus().name())

                                .scanEngine(
                                                domain.getScanEngine())

                                .scannedAt(
                                                domain.getScannedAt())

                                .origin(
                                                domain.getOrigin())

                                .documentType(
                                                domain.getDocumentType())

                                .detectedContentType(
                                                domain.getDetectedContentType())

                                .bucket(
                                                domain.getBucket())

                                .objectKey(
                                                domain.getObjectKey())

                                .contentType(
                                                domain.getContentType())

                                .fileName(
                                                domain.getFileName())

                                .build();
        }
}