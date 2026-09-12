package com.govtech.apply.application.event;

import org.springframework.stereotype.Service;

import com.govtech.apply.application.service.ApplicationOutboxService;
import com.govtech.apply.domain.model.Application;
import com.govtech.platform.messaging.event.EventContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationEventService {

    private final ApplicationOutboxService outboxService;
    private final ApplicationEventFactory eventFactory;

    public void publish(
            Application application) {

        outboxService.publish(
                application,
                eventFactory::buildApplicationSubmitted,
                "ApplicationSubmitted",
                "application.submitted");
    }

    public void publish(
            Application application, EventContext eventContext) {

        outboxService.publish(
                application,
                eventContext,
                eventFactory::buildApplicationGenerated,
                "ApplicationGenerated",
                "application.generated");
    }
}