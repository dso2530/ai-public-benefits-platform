package com.govtech.bff.dashboard.dto;

public record HouseholdDto(
        String city,
        String housingStatus,
        Integer children,
        Boolean singleParent
) {
}