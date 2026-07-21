package com.govtech.eligibility.application.mapper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.govtech.eligibility.domain.model.Eligibility;
import com.govtech.events.common.BaseEvent;
import com.govtech.events.eligibility.EligibilityCheckedEvent;
import com.govtech.events.eligibility.EligibilityResult;

public final class EligibilityCheckedEventMapper {

        private EligibilityCheckedEventMapper() {
        }

        public static EligibilityCheckedEvent toEvent(
                        String subject,
                        List<Eligibility> results) {

                return EligibilityCheckedEvent.newBuilder()

                                .setMetadata(
                                                BaseEvent.newBuilder()
                                                                .setSubject(subject)
                                                                .setOccurredAt(Instant.now().toString())
                                                                .setEventId(UUID.randomUUID().toString())
                                                                .build())

                                .setEligibilities(
                                                results.stream()
                                                                .map(EligibilityCheckedEventMapper::toResult)
                                                                .toList())

                                .build();

        }

        private static EligibilityResult toResult(Eligibility eligibility) {

                return EligibilityResult.newBuilder()
                                .setAidCode(eligibility.aidCode())
                                .setAidName(eligibility.aidName())
                                .setEligible(eligibility.status().isEligible())
                                .setReason(eligibility.reason())
                                .setEstimatedAmount(
                                                eligibility.estimatedAmount() == null
                                                                ? null
                                                                : eligibility.estimatedAmount().toString())
                                .build();
        }

}