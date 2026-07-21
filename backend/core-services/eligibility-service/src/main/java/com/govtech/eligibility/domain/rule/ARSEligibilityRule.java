package com.govtech.eligibility.domain.rule;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.govtech.eligibility.application.configuration.EligibilityProperties;
import com.govtech.eligibility.application.dto.ProfileContractDto;
import com.govtech.eligibility.domain.model.Eligibility;
import com.govtech.eligibility.domain.model.EligibilityStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ARSEligibilityRule implements EligibilityRule {

    private final EligibilityProperties properties;

    @Override
    public Optional<Eligibility> evaluate(ProfileContractDto profile,
            UUID calculationId,
            Instant calculatedAt) {

        log.debug("Evaluating ARS for subject={}", profile.subject());

        if (!isEligible(profile)) {
            return Optional.of(
                    Eligibility.builder()
                            .calculationId(calculationId)
                            .calculatedAt(calculatedAt)
                            .subject(profile.subject())
                            .aidCode("ARS")
                            .aidName("Allocation de rentrée scolaire")
                            .status(EligibilityStatus.NOT_ELIGIBLE)
                            .reason("Les critères d'éligibilité ne sont pas remplis.")
                            .build());
        }

        return Optional.of(
                Eligibility.builder()
                        .calculationId(calculationId)
                        .calculatedAt(calculatedAt)
                        .subject(profile.subject())
                        .aidCode("ARS")
                        .aidName("Allocation de rentrée scolaire")
                        .status(EligibilityStatus.ELIGIBLE)
                        .estimatedAmount(new BigDecimal(properties.getArs().getEstimatedAmount()))
                        .estimatedAmountLabel(
                                properties.getArs().getEstimatedAmountLabel())
                        .reason("Le foyer est éligible à l'allocation de rentrée scolaire.")
                        .build());
    }

    private boolean isEligible(ProfileContractDto profile) {

        if (profile.referenceIncome() == null || profile.childrenCount() == null) {
            return false;
        }

        BigDecimal income = ProfileContractDto.parse(profile.referenceIncome());

        return profile.childrenCount() > 0
                && income.compareTo(properties.getArs().getIncomeCeiling()) <= 0;
    }
}