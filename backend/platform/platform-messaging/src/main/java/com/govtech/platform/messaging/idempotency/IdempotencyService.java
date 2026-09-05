package com.govtech.platform.messaging.idempotency;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotencyStore store;

    public boolean tryAcquire(
            String consumerGroup,
            String eventId) {

        return store.tryAcquire(
                consumerGroup,
                eventId);
    }

    public void markProcessed(
            String consumerGroup,
            String eventId) {

        store.markProcessed(
                consumerGroup,
                eventId);
    }

    public void release(
            String consumerGroup,
            String eventId) {

        store.release(
                consumerGroup,
                eventId);
    }
}