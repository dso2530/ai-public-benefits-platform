package com.govtech.bff.eligibility.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder
public record EligibilityDto(
    String aidCode,
    String aidName,
    String description,
    String category,
    String status,
    BigDecimal estimatedAmount,
    String estimatedAmountLabel,
    String reason,
    boolean canApply,
    String actionLabel,
    String icon,
    UUID applicationId) {}
