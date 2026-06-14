package com.govtech.bff.dto;

public record BenefitsDto(
        Integer eligibleCount,
        Integer pendingCount,
        Integer totalAmount
) {
}