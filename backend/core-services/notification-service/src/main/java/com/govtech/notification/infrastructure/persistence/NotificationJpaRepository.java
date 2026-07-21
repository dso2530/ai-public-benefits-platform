package com.govtech.notification.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository
    extends JpaRepository<NotificationJpaEntity, UUID> {

  List<NotificationJpaEntity> findBySubjectOrderByCreatedAtDesc(String subject);

  long countBySubject(String subject);

  long countBySubjectAndReadFalse(String subject);

  Optional<NotificationJpaEntity> findByIdAndSubject(UUID id, String subject);
}
