package com.govtech.eligibility.api.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record EligibilitySummaryDto(long eligibleCount, long notEligibleCount, BigDecimal totalAmount) {
}
