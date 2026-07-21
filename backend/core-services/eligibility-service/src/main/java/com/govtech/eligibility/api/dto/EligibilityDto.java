package com.govtech.eligibility.api.dto;

import java.math.BigDecimal;

import com.govtech.eligibility.domain.model.Eligibility;
import com.govtech.eligibility.domain.model.EligibilityStatus;

import lombok.Builder;

@Builder
public record EligibilityDto(
        String aidCode,
        String aidName,
        String status,
        String reason,
        BigDecimal estimatedAmount,
        String estimatedAmountLabel,
        boolean canApply,
        String actionLabel,
        String icon) {

    public static EligibilityDto from(Eligibility eligibility) {

        boolean canApply = eligibility.status() == EligibilityStatus.ELIGIBLE;

        return EligibilityDto.builder()
                .aidCode(eligibility.aidCode())
                .aidName(eligibility.aidName())
                .status(eligibility.status().name())
                .reason(eligibility.reason())
                .estimatedAmount(eligibility.estimatedAmount())
                .estimatedAmountLabel(eligibility.estimatedAmountLabel())
                .canApply(canApply)
                .actionLabel(actionLabel(eligibility))
                .icon("home")
                .build();
    }

    private static String actionLabel(Eligibility source) {
        return switch (source.status()) {
            case EligibilityStatus.ELIGIBLE -> "Faire la demande";

            /*
             * case "PENDING" -> "Compléter le dossier";
             * case "IN_REVIEW" -> "Suivre la demande";
             * case "REJECTED" -> "Voir le motif";
             */
            default -> "Consulter";
        };
    }
}