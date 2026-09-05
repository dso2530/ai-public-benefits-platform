package com.govtech.citizen.dashboard.dto;

import lombok.Builder;

@Builder
public record HouseholdDto(
    String city, String housingStatus, Integer children, Boolean singleParent) {}
