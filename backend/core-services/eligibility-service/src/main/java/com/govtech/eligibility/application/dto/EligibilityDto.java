package com.govtech.eligibility.application.dto;

import java.math.BigDecimal;

public record EligibilityDto(
        String code,
        String name,
        BigDecimal amount
) {
}