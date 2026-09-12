package com.govtech.platform.messaging.outbox;

import java.time.Instant;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.govtech.platform.messaging.publisher.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "messaging.outbox", name = "enabled", havingValue = "true", matchIfMissing = false)
public class OutboxPublisher {

    private final OutboxStore store;
    private final EventPublisher eventPublisher;
    private final OutboxProperties properties;

    @Scheduled(fixedDelayString = "${messaging.outbox.fixed-delay:PT5S}")
    public void publish() {

        if (!properties.enabled()) {
            return;
        }

        recoverStaleEvents();

        List<OutboxEvent> events = store.claim(properties.batchSize());

        for (OutboxEvent event : events) {
            publish(event);
        }
    }

    private void recoverStaleEvents() {

        Instant threshold = Instant.now()
                .minus(properties.staleAfter());

        int recovered = store.recoverStaleEvents(threshold);

        if (recovered > 0) {

            log.warn(
                    "Recovered {} stale outbox events older than {}",
                    recovered,
                    threshold);
        }
    }

    private void publish(OutboxEvent event) {

        try {

            eventPublisher.publish(
                    event.topic(),
                    event.eventId(),
                    event.payload());

            store.markAsPublished(
                    event.eventId());

            log.debug(
                    "Outbox event published eventId={} topic={}",
                    event.eventId(),
                    event.topic());

        } catch (Exception exception) {

            log.error(
                    "Failed to publish outbox event eventId={} topic={}",
                    event.eventId(),
                    event.topic(),
                    exception);

            store.markAsFailed(
                    event.eventId(),
                    exception.getMessage());
        }
    }
}