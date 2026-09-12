package com.govtech.knowledge.application.mapper;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.govtech.events.document.DocumentExtractionCompletedEvent;
import com.govtech.knowledge.application.command.KnowledgeDocumentIndexCommand;

@Component
public class DocumentIndexEventMapper {

    public KnowledgeDocumentIndexCommand toCommand(
            DocumentExtractionCompletedEvent event) {

        return new KnowledgeDocumentIndexCommand(

                event.getBaseEvent().getDocumentId(),

                event.getBaseEvent().getSource(),

                event.getTerritoryCode(),

                event.getBaseEvent().getDocumentType().name(),

                event.getBaseEvent().getBucket(),

                event.getBaseEvent().getObjectKey(),

                event.getBaseEvent().getContentType(),

                event.getText(),

                event.getBaseEvent().getSha256(),

                parseDate(event.getProcessedAt())

        );

    }

    private Instant parseDate(
            String value) {

        return value == null
                ? Instant.now()
                : Instant.parse(value);

    }

}