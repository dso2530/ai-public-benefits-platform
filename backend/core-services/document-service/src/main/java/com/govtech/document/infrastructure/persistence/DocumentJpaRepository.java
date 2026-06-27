package com.govtech.document.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentJpaRepository extends JpaRepository<DocumentJpaEntity, Long> {

  List<DocumentJpaEntity> findBySubjectOrderByUploadedAtDesc(String subject);

  long countBySubject(String subject);

  long countBySubjectAndStatus(String subject, String status);
}
