package com.govtech.application.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationPackageRepository
        extends JpaRepository<ApplicationPackageJpaEntity, UUID> {

    Optional<ApplicationPackageJpaEntity> findByApplicationId(UUID applicationId);

    boolean existsByApplicationId(UUID applicationId);
}