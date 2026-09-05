package com.govtech.documentextra.application.event;

import org.springframework.stereotype.Service;

import com.govtech.documentextra.application.command.DocumentExtractionCommand;
import com.govtech.documentextra.application.service.DocumentExtractionOutboxService;
import com.govtech.documentextra.domain.model.ExtractionResult;
import com.govtech.platform.messaging.event.EventContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentExtractionEventService {

    private final DocumentExtractionOutboxService outboxService;
    private final DocumentExtractionEventFactory eventFactory;

    public void publishExtractionCompleted(
            DocumentExtractionCommand command,
            ExtractionResult result,
            EventContext eventContext) {

        outboxService.publish(
                command,
                eventContext,
                (cmd, context) -> eventFactory.buildExtractionCompleted(
                        cmd,
                        result,
                        context),
                "DocumentExtractionCompleted",
                "document.extraction.completed");
    }
}