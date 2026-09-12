package com.govtech.document.application.service;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.govtech.document.domain.model.Document;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.outbox.OutboxEvent;
import com.govtech.platform.messaging.outbox.OutboxEventFactory;
import com.govtech.platform.messaging.outbox.OutboxStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentOutboxService {

        private static final String AGGREGATE_TYPE = "DOCUMENT";

        private final OutboxEventFactory outboxEventFactory;
        private final OutboxStore outboxStore;

        /**
         * Publication d'un événement ne nécessitant pas
         * de contexte de causalité explicite.
         */
        public <E> void publish(
                        Document document,
                        Function<Document, E> eventBuilder,
                        String eventType,
                        String topic) {

                E event = eventBuilder.apply(document);

                save(
                                document,
                                event,
                                eventType,
                                topic);
        }

        /**
         * Publication d'un événement faisant partie
         * d'une chaîne de causalité.
         */
        public <E> void publish(
                        Document document,
                        EventContext context,
                        BiFunction<Document, EventContext, E> eventBuilder,
                        String eventType,
                        String topic) {

                E event = eventBuilder.apply(
                                document,
                                context);

                save(
                                document,
                                event,
                                eventType,
                                topic);
        }

        private <E> void save(
                        Document document,
                        E event,
                        String eventType,
                        String topic) {

                OutboxEvent outboxEvent = outboxEventFactory.create(
                                eventType,
                                topic,
                                AGGREGATE_TYPE,
                                document.getId().toString(),
                                event);

                outboxStore.save(outboxEvent);
        }
}