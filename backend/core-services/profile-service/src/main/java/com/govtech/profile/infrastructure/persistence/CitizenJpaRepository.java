package com.govtech.profile.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CitizenJpaRepository
        extends JpaRepository<CitizenJpaEntity, UUID> {

    Optional<CitizenJpaEntity> findBySubject(
            String subject);

    Optional<CitizenJpaEntity> findByEmail(
            String email);

    boolean existsBySubject(
            String subject);
}