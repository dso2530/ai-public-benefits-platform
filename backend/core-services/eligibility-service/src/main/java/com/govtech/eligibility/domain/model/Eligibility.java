package com.govtech.eligibility.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.Builder;

@Builder
public record Eligibility(UUID id,
        UUID calculationId,
        Instant calculatedAt,
        String subject,
        String aidCode,
        String aidName,
        EligibilityStatus status,
        String reason,
        BigDecimal estimatedAmount,
        String estimatedAmountLabel) {
}
