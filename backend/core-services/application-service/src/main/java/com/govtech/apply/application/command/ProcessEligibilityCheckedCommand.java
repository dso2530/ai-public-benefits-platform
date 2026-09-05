package com.govtech.apply.application.command;

import java.time.Instant;
import java.util.List;

  
import lombok.Builder;

@Builder
public record ProcessEligibilityCheckedCommand(
        String subject,
        List<EligibilityCommandItem> eligibilities,
        Instant occurredAt) {
}