package com.govtech.eligibility.application.dto;

import java.math.BigDecimal;

public record EligibilitySummaryDto(
        int eligibleCount,
        int pendingCount,
        BigDecimal totalAmount
) {
}