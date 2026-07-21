package com.govtech.bff.dashboard.dto;

import java.math.BigDecimal;

public record EligibilitySummaryDto(
    long eligibleCount, long notEligibleCount, BigDecimal totalAmount) {}
