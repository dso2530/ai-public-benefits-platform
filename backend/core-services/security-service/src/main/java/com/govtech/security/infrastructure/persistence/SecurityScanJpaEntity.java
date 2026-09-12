package com.govtech.security.infrastructure.persistence;

import java.time.Instant;
import java.util.UUID;

import com.govtech.shared.model.DocumentOrigin;
import com.govtech.shared.model.DocumentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "security_scans")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityScanJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private Long documentId;

    @Column(length = 64)
    private String sha256;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(length = 50)
    private String scanEngine;

    @Column(name = "detected_content_type", length = 100)
    private String detectedContentType;

    @Column(nullable = false)
    private Instant scannedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentOrigin origin;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 50)
    private DocumentType documentType;

    @Column(length = 255)
    private String bucket;

    @Column(name = "object_key", length = 1000)
    private String objectKey;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "file_name", length = 500)
    private String fileName;
}