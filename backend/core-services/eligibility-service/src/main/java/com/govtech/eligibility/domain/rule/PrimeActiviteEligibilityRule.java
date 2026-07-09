package com.govtech.eligibility.domain.rule;

import com.govtech.eligibility.application.configuration.EligibilityProperties;
import com.govtech.eligibility.application.dto.ProfileContractDto;
import com.govtech.eligibility.domain.model.Eligibility;
import com.govtech.eligibility.domain.model.EligibilityStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PrimeActiviteEligibilityRule implements EligibilityRule {

        private final EligibilityProperties properties;

        @Override
        public Optional<Eligibility> evaluate(ProfileContractDto profile,
                        UUID calculationId,
                        Instant calculatedAt) {

                log.debug("Evaluating Prime d'activité for subject={}", profile.subject());

                boolean eligible = profile.referenceIncome() != null
                                && ProfileContractDto.parse(profile.referenceIncome())
                                                .compareTo(properties.getPrimeActivite().getIncomeCeiling()) <= 0;

                if (!eligible) {
                        return Optional.of(Eligibility.builder()
                                        .calculationId(calculationId)
                                        .calculatedAt(calculatedAt)
                                        .subject(profile.subject())
                                        .aidCode("PRIME_ACTIVITE")
                                        .aidName("Prime d'activité")
                                        .status(EligibilityStatus.NOT_ELIGIBLE)
                                        .reason("Les revenus dépassent le plafond.")
                                        .build());
                }

                return Optional.of(Eligibility.builder()
                                .calculationId(calculationId)
                                .calculatedAt(calculatedAt)
                                .subject(profile.subject())
                                .aidCode("PRIME_ACTIVITE")
                                .aidName("Prime d'activité")
                                .status(EligibilityStatus.ELIGIBLE)
                                .estimatedAmount(properties.getPrimeActivite().getEstimatedAmount())
                                .reason("Le profil satisfait les critères principaux.")
                                .build());
        }
}