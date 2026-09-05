package com.govtech.platform.messaging.idempotency;

public interface IdempotencyStore {

    boolean tryAcquire(
            String consumerGroup,
            String eventId);

    void markProcessed(
            String consumerGroup,
            String eventId);

    void release(
            String consumerGroup,
            String eventId);
}