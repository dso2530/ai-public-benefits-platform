package com.govtech.eligibility.domain.rule;

import com.govtech.eligibility.application.configuration.EligibilityProperties;
import com.govtech.eligibility.application.dto.ProfileContractDto;
import com.govtech.eligibility.domain.model.Eligibility;
import com.govtech.eligibility.domain.model.EligibilityStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class APLEligibilityRule implements EligibilityRule {

    private final EligibilityProperties properties;

    @Override
    public Optional<Eligibility> evaluate(ProfileContractDto profile,
            UUID calculationId,
            Instant calculatedAt) {

        log.debug("Evaluating APL for subject={}", profile.subject());

        if (!isEligible(profile)) {

            return Optional.of(Eligibility.builder()
                    .calculationId(calculationId)
                    .calculatedAt(calculatedAt)
                    .subject(profile.subject())
                    .aidCode("APL")
                    .aidName("Aide personnalisée au logement")
                    .status(EligibilityStatus.NOT_ELIGIBLE)
                    .reason("Les critères d'éligibilité ne sont pas remplis.")
                    .build());
        }

        return Optional.of(Eligibility.builder()
                .calculationId(calculationId)
                .calculatedAt(calculatedAt)
                .subject(profile.subject())
                .aidCode("APL")
                .aidName("Aide personnalisée au logement")
                .status(EligibilityStatus.ELIGIBLE)
                .estimatedAmount(properties.getApl().getEstimatedAmount())
                .reason("Le profil satisfait les critères principaux.")
                .build());
    }

    private boolean isEligible(ProfileContractDto profile) {

        if (profile.referenceIncome() == null) {
            return false;
        }

        if (profile.housingStatus() == null) {
            return false;
        }

        // Exemple de règle simplifiée
        return !"OWNER".equals(profile.housingStatus())
                && ProfileContractDto.parse(profile.referenceIncome())
                        .compareTo(properties.getApl().getIncomeCeiling()) <= 0;
    }
}