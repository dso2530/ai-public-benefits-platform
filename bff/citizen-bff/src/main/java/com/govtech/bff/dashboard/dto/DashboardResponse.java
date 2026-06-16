package com.govtech.bff.dashboard.dto;
import lombok.Builder;

@Builder
public record DashboardResponse(
        HouseholdDto household,
        BenefitsDto benefits,
        NotificationsDto notifications
) {
}