package com.govtech.platform.messaging.outbox;

import java.time.Instant;
import java.util.List;

public interface OutboxStore {

    OutboxEvent save(OutboxEvent event);

    List<OutboxEvent> claim(int batchSize);

    void markAsPublished(String eventId);

    void markAsFailed(
            String eventId,
            String error);

    int recoverStaleEvents(Instant threshold);
}