package com.govtech.application.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.govtech.application.domain.model.ApplicationStatus;

public interface ApplicationJpaRepository
    extends JpaRepository<ApplicationJpaEntity, UUID> {

  List<ApplicationJpaEntity> findBySubjectOrderByCreatedAtDesc(String subject);

  long countBySubject(String subject);

  long countBySubjectAndStatus(String subject, ApplicationStatus status);

  Optional<ApplicationJpaEntity> findByApplicationIdAndSubject(UUID id, String subject);

}