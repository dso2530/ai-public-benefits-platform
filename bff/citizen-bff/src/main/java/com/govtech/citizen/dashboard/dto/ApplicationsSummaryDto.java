package com.govtech.citizen.dashboard.dto;

import lombok.Builder;

@Builder
public record ApplicationsSummaryDto(
    int total, int generated, int readyToSubmit, int submitted, int accepted, int rejected) {}
