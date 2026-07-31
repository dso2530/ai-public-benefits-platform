package com.govtech.consent.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "consents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsentJpaEntity {

    @Id
    private UUID id;

    private UUID userId;

    private String purpose;

    private String version;

    @Enumerated(EnumType.STRING)
    private ConsentStatusJpa status;

    /**
     * Référence du texte accepté
     */
    private UUID consentVersionId;

    private Instant grantedAt;

    private Instant revokedAt;

    private String source;

}