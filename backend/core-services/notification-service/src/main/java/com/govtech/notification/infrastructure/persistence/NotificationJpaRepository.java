package com.govtech.notification.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<NotificationJpaEntity, Long> {

  List<NotificationJpaEntity> findBySubjectOrderByCreatedAtDesc(String subject);

  long countBySubject(String subject);

  long countBySubjectAndReadFalse(String subject);
}
