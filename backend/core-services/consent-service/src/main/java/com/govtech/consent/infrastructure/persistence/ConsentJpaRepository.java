package com.govtech.consent.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentJpaRepository
                extends JpaRepository<ConsentJpaEntity, UUID> {

        Optional<ConsentJpaEntity> findByUserIdAndPurpose(
                        UUID userId,
                        String purpose);

}