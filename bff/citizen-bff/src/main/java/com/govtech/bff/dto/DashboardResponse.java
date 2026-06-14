package com.govtech.bff.dto;

public record DashboardResponse(
        HouseholdDto household,
        BenefitsDto benefits,
        NotificationsDto notifications
) {
}