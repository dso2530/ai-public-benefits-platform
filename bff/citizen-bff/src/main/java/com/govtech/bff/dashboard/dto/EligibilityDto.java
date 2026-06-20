package com.govtech.bff.dashboard.dto;

import java.math.BigDecimal;

public record EligibilityDto(
        String code,
        String name,
        BigDecimal amount
) {
}