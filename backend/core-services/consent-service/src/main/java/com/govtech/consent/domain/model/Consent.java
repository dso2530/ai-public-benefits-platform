package com.govtech.consent.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class Consent {

    private final UUID id;

    private final UUID userId;

    /**
     * Exemple :
     * CAF_APPLICATION
     * DOCUMENT_ANALYSIS
     * AI_PROCESSING
     */
    private final String purpose;

    /**
     * Version du texte de consentement accepté
     */
    private final String version;

    private final ConsentStatus status;

    private final Instant grantedAt;

    private final Instant revokedAt;

    private final String source;

    private final UUID consentVersionId;

    public boolean isGranted() {
        return status == ConsentStatus.GRANTED;
    }

    public boolean canProcess() {
        return isGranted()
                && revokedAt == null;
    }
}