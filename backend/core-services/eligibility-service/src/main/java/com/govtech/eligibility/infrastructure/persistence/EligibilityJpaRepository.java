package com.govtech.eligibility.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.govtech.eligibility.infrastructure.persistence.entity.EligibilityJpaEntity;

public interface EligibilityJpaRepository
        extends JpaRepository<EligibilityJpaEntity, UUID> {

    List<EligibilityJpaEntity> findBySubjectOrderByCalculatedAtDesc(String subject);

    List<EligibilityJpaEntity> findByCalculationId(UUID calculationId);
}