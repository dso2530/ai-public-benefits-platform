package com.govtech.apply.infrastructure.persistence;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.govtech.apply.domain.model.ApplicationStatus;
import com.govtech.shared.model.DocumentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "applications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationJpaEntity {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false, unique = true)
  private UUID applicationId;

  @Column(nullable = false)
  private String subject;

  @Column(nullable = false)
  private String aidCode;

  @Column(nullable = false)
  private String aidName;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ApplicationStatus status;

  @Column(nullable = false)
  private Instant createdAt;

  private String objectKey;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private List<DocumentType> missingDocuments;

}