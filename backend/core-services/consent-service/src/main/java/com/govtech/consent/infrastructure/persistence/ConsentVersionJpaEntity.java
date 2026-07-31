package com.govtech.consent.infrastructure.persistence;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "consent_versions", uniqueConstraints = {
        @UniqueConstraint(name = "uk_consent_version", columnNames = {
                "purpose",
                "version"
        })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentVersionJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 100)
    private String purpose;

    @Column(nullable = false, length = 20)
    private String version;

    /**
     * Texte juridique affiché
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * Hash du texte pour preuve d'intégrité
     */
    @Column(nullable = false, length = 64)
    private String sha256;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant activatedAt;

    private Instant retiredAt;

}