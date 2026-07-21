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
import java.util.UUID;

import com.govtech.document.domain.model.DocumentType;

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

  private String fileName;

  private String contentType;

  @Column(nullable = false)
  private String objectKey;

  private Long fileSize;

  @Column(nullable = false)
  private Instant uploadedAt;

  @Column(name = "application_id")
  private UUID applicationId;
}
