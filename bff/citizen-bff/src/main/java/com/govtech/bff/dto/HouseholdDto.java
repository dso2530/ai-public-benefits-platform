package com.govtech.bff.dto;

public record HouseholdDto(
        String city,
        String housingStatus,
        Integer children,
        Boolean singleParent
) {
}