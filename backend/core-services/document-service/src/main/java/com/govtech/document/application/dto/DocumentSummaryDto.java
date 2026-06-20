package com.govtech.document.application.dto;
public record DocumentSummaryDto(
        int total,
        int validated,
        int pending
) {}