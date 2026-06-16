package com.govtech.bff.dashboard.dto;

public record BenefitsDto(
        Integer eligibleCount,
        Integer pendingCount,
        Integer totalAmount
) {
}