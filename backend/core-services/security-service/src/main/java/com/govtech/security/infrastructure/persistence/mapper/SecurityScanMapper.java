package com.govtech.security.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.govtech.security.domain.model.SecurityScan;
import com.govtech.security.domain.model.SecurityStatus;
import com.govtech.security.infrastructure.persistence.SecurityScanJpaEntity;


@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public interface SecurityScanMapper {


    default SecurityScan toDomain(
            SecurityScanJpaEntity entity) {


        if (entity == null) {
            return null;
        }


        return SecurityScan.builder()

                .id(entity.getId())

                .documentId(
                        entity.getDocumentId()
                )

                .sha256(
                        entity.getSha256()
                )

                .status(
                        SecurityStatus.valueOf(
                                entity.getStatus()
                        )
                )

                .scanEngine(
                        entity.getScanEngine()
                )

                .scannedAt(
                        entity.getScannedAt()
                )

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
                        domain.getDocumentId()
                )

                .sha256(
                        domain.getSha256()
                )

                .status(
                        domain.getStatus().name()
                )

                .scanEngine(
                        domain.getScanEngine()
                )

                .scannedAt(
                        domain.getScannedAt()
                )

                .build();
    }

}