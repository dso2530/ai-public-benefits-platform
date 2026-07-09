package com.govtech.eligibility.api.dto;

import java.math.BigDecimal;

public record EligibilitySummaryDto(int eligibleCount, int pendingCount, BigDecimal totalAmount) {}
