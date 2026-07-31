package com.govtech.security.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.govtech.security.domain.model.SecurityScan;
import com.govtech.security.domain.port.SecurityScanRepositoryPort;
import com.govtech.security.infrastructure.persistence.mapper.SecurityScanMapper;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class SecurityScanJpaAdapter
        implements SecurityScanRepositoryPort {


    private final SecurityScanJpaRepository repository;

    private final SecurityScanMapper mapper;



    @Override
    public SecurityScan save(
            SecurityScan scan) {


        SecurityScanJpaEntity entity =
                mapper.toEntity(scan);


        return mapper.toDomain(
                repository.save(entity)
        );
    }



    public Optional<SecurityScan> findLatestByDocumentId(
            Long documentId) {


        return repository
                .findFirstByDocumentIdOrderByScannedAtDesc(
                        documentId
                )
                .map(mapper::toDomain);

    }

}