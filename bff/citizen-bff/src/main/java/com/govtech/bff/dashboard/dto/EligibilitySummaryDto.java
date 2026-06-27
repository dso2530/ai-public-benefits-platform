package com.govtech.bff.dashboard.dto;

public record EligibilitySummaryDto(
    Integer eligibleCount, Integer pendingCount, Integer totalAmount) {}
