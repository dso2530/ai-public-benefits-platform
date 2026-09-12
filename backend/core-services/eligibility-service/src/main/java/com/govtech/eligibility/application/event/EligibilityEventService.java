package com.govtech.eligibility.application.event;

import java.util.List;

import org.springframework.stereotype.Service;

import com.govtech.eligibility.application.service.EligibilityOutboxService;
import com.govtech.eligibility.domain.model.Eligibility;
import com.govtech.platform.messaging.event.EventContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EligibilityEventService {

    private final EligibilityOutboxService outboxService;
    private final EligibilityEventFactory eventFactory;

    public void publishEligibilityChecked(
            String subject,
            List<Eligibility> results,
            EventContext eventContext) {

        outboxService.publish(
                subject,
                eventContext,
                (aggregateId, context) ->
                        eventFactory.buildEligibilityChecked(
                                subject,
                                results,
                                context),
                "EligibilityChecked",
                "eligibility.checked");
    }
}