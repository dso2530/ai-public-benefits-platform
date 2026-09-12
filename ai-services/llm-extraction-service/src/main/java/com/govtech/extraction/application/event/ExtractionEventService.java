package com.govtech.extraction.application.event;

import org.springframework.stereotype.Service;

import com.govtech.extraction.domain.model.Document;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.publisher.EventPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExtractionEventService {

        private final EventPublisher eventPublisher;
        private final DocumentEventFactory eventFactory;

        public void publishExtractionCompleted(
                        Document document,
                        String extractedData,
                        String model,
                        EventContext eventContext) {

                var event = eventFactory.create(
                                document,
                                extractedData,
                                model,
                                eventContext);

                eventPublisher.publish(
                                document.type().topic(),
                                document.objectKey(),
                                event);
        }
}