package com.govtech.apply.application.command;

import lombok.Builder;

@Builder
public record EligibilityCommandItem(
        String aidCode,
        String aidName,
        Boolean eligible) {
}