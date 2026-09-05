package com.govtech.document.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.govtech.shared.model.DocumentOrigin;
import com.govtech.shared.model.DocumentType;
import com.govtech.shared.model.SecurityStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "documents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String subject;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String status;

  @Enumerated(EnumType.STRING)
  private DocumentType documentType;

  @Enumerated(EnumType.STRING)
  @Column(name = "origin", nullable = false)
  private DocumentOrigin origin;

  @Column(nullable = false, length = 100)
  private String source;

  private String fileName;

  /**
   * Empreinte SHA-256 du fichier stocké dans MinIO
   */
  @Column(length = 64)
  private String sha256;

  private String contentType;

  @Column(nullable = false)
  private String bucket;

  @Column(nullable = false)
  private String objectKey;

  private Long fileSize;

  @Column(nullable = false)
  private Instant uploadedAt;

  @Column(name = "application_id")
  private UUID applicationId;

  @Enumerated(EnumType.STRING)
  @Column(name = "security_status", nullable = false)
  private SecurityStatus securityStatus;

  private Instant scannedAt;

  @Column(length = 50)
  private String scanEngine;

  @Column(name = "detected_content_type")
  private String detectedContentType;

  @Column(length = 100)
  private String connectorName;

  @Column(length = 50)
  private String connectorType;

  @Column(length = 50)
  private String territoryCode;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Map<String, String> metadata;
}
