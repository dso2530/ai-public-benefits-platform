package com.govtech.citizen.dashboard.dto;

import lombok.Builder;

@Builder
public record DashboardResponse(
    HouseholdDto household,
    EligibilitySummaryDto benefits,
    NotificationSummaryDto notifications,
    DocumentSummaryDto documents,
    ApplicationsSummaryDto applications) {}
