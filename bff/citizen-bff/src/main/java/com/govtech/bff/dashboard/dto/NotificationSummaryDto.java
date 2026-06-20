package com.govtech.bff.dashboard.dto;

public record NotificationSummaryDto(
        int total,
        int unread
) {
}