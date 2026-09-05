package com.govtech.platform.messaging.outbox;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "messaging.outbox", name = "enabled", havingValue = "true", matchIfMissing = false)
public class OutboxService {

    private final OutboxStore store;

    public OutboxEvent save(
            OutboxEvent event) {

        return store.save(event);
    }
}