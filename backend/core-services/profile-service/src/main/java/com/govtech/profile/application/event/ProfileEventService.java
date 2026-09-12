package com.govtech.profile.application.event;

import org.springframework.stereotype.Service;

import com.govtech.platform.messaging.event.EventContext;
import com.govtech.profile.application.dto.DocumentCommand;
import com.govtech.profile.application.service.ProfileOutboxService;
import com.govtech.profile.domain.model.Citizen;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileEventService {

    private final ProfileOutboxService outboxService;
    private final ProfileEventFactory eventFactory;

    public void publishProfileUpdated(
            Citizen citizen,
            EventContext eventContext) {

        outboxService.publish(
                citizen,
                eventContext,
                eventFactory::buildProfileUpdated,
                "ProfileUpdated",
                "profile.updated");
    }

    public void publishSupportingDocumentUpdated(
            Citizen citizen,
            DocumentCommand documentCommand,
            EventContext eventContext) {

        outboxService.publish(
                citizen,
                eventContext,
                (c, context) -> eventFactory.buildSupportingDocumentUpdated(
                        c,
                        documentCommand,
                        context),
                "SupportingDocumentUpdated",
                "supporting.document.updated");
    }
}