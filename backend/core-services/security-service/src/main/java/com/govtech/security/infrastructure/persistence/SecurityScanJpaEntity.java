package com.govtech.security.infrastructure.persistence;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

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

}