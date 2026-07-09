package com.govtech.eligibility.api.dto;

import java.math.BigDecimal;

import com.govtech.eligibility.domain.model.Eligibility;

import lombok.Builder;

@Builder
public record EligibilityDto(
        String aidCode,
        String aidName,
        String status,
        String reason,
        String estimatedAmount) {

    public static EligibilityDto from(Eligibility eligibility) {
        return EligibilityDto.builder()
                .aidCode(eligibility.aidCode())
                .aidName(eligibility.aidName())
                .status(eligibility.status().name())
                .reason(eligibility.reason())
                .estimatedAmount(eligibility.estimatedAmount())
                .build();
    }
}