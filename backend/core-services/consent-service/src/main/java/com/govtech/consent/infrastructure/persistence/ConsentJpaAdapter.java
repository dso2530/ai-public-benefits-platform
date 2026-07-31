package com.govtech.consent.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.govtech.consent.domain.model.Consent;
import com.govtech.consent.domain.port.ConsentRepositoryPort;
import com.govtech.consent.infrastructure.persistence.mapper.ConsentMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ConsentJpaAdapter implements ConsentRepositoryPort {

    private final ConsentJpaRepository repository;

    private final ConsentMapper mapper;

    @Override
    public Consent save(
            Consent consent) {

        ConsentJpaEntity entity = mapper.toEntity(consent);

        return mapper.toDomain(
                repository.save(entity));
    }

    @Override
    public Optional<Consent> findByUserIdAndPurpose(
            UUID userId,
            String purpose) {

        return repository
                .findByUserIdAndPurpose(
                        userId,
                        purpose)
                .map(mapper::toDomain);

    }

}