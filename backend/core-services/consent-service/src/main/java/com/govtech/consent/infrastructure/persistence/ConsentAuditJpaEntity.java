package com.govtech.consent.infrastructure.persistence;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "consent_audit", indexes = {
        @Index(name = "idx_consent_audit_consent", columnList = "consent_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentAuditJpaEntity {

    @Id
    private UUID id;

    @Column(name = "consent_id", nullable = false)
    private UUID consentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ConsentAuditActionJpa action;

    /**
     * Qui a réalisé l'action
     */
    @Column(length = 100)
    private String actor;

    /**
     * Adresse IP lors du consentement
     */
    @Column(length = 45)
    private String ipAddress;

    /**
     * Navigateur / application mobile
     */
    @Column(length = 255)
    private String userAgent;

    /**
     * Données complémentaires
     */
    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Column(nullable = false)
    private Instant createdAt;

}