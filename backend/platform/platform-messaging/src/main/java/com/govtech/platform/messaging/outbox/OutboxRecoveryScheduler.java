package com.govtech.platform.messaging.outbox;

import lombok.RequiredArgsConstructor;

import java.time.Instant;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "messaging.outbox", name = "enabled", havingValue = "true", matchIfMissing = false)
public class OutboxRecoveryScheduler {

    private final OutboxStore outboxStore;

    private final OutboxProperties properties;

    @Scheduled(fixedDelayString = "${messaging.outbox.recovery-delay:PT1M}")
    public void recover() {

        if (!properties.enabled()) {
            return;
        }

        Instant threshold = Instant.now()
                .minus(properties.staleAfter());

        outboxStore.recoverStaleEvents(threshold);
    }
}