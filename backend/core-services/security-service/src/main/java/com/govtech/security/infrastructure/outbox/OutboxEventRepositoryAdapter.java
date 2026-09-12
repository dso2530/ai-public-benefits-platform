package com.govtech.security.infrastructure.outbox;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.platform.database.outbox.OutboxEventJpaEntity;
import com.govtech.platform.database.outbox.OutboxEventJpaRepository;
import com.govtech.platform.database.outbox.OutboxStatusJpaEntity;
import com.govtech.platform.messaging.event.AvroEventTypeRegistry;
import com.govtech.platform.messaging.outbox.OutboxEvent;
import com.govtech.platform.messaging.outbox.OutboxStatus;
import com.govtech.platform.messaging.outbox.OutboxStore;
import com.govtech.platform.messaging.serialization.EventDeserializer;
import com.govtech.platform.messaging.serialization.EventSerializer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutboxEventRepositoryAdapter implements OutboxStore {

        private final OutboxEventJpaRepository repository;

        private final EventSerializer eventSerializer;

        private final EventDeserializer eventDeserializer;

        private final AvroEventTypeRegistry eventTypeRegistry;

        private OutboxEventJpaEntity toJpaEntity(
                        OutboxEvent event) {

                byte[] payload = eventSerializer.serialize(
                                event.payload());

                return OutboxEventJpaEntity.create(
                                event.eventId(),
                                event.eventType(),
                                event.topic(),
                                event.aggregateType(),
                                event.aggregateId(),
                                payload);
        }

        private OutboxEvent toDomain(
                        OutboxEventJpaEntity entity) {

                Class<?> eventClass = eventTypeRegistry.resolve(
                                entity.getEventType());

                Object payload = eventDeserializer.deserialize(
                                entity.getPayload(),
                                eventClass);

                return OutboxEvent.builder()
                                .id(entity.getId())
                                .eventId(entity.getEventId())
                                .eventType(entity.getEventType())
                                .topic(entity.getTopic())
                                .aggregateType(entity.getAggregateType())
                                .aggregateId(entity.getAggregateId())
                                .payload(payload)
                                .status(
                                                OutboxStatus.valueOf(
                                                                entity.getStatus().name()))
                                .createdAt(entity.getCreatedAt())
                                .publishedAt(entity.getPublishedAt())
                                .attempts(entity.getAttempts())
                                .lastError(entity.getLastError())
                                .build();
        }

        @Override
        @Transactional
        public OutboxEvent save(
                        OutboxEvent event) {

                OutboxEventJpaEntity entity = repository.save(
                                toJpaEntity(event));

                return toDomain(entity);
        }

        @Override
        @Transactional
        public List<OutboxEvent> claim(
                        int batchSize) {

                List<OutboxEventJpaEntity> entities = repository.claimReadyToPublish(batchSize);

                Instant now = Instant.now();

                for (OutboxEventJpaEntity entity : entities) {

                        repository.markAsPublishing(
                                        entity.getEventId());

                        entity.setStatus(
                                        OutboxStatusJpaEntity.PUBLISHING);

                        entity.setClaimedAt(now);
                }

                return entities.stream()
                                .map(this::toDomain)
                                .toList();
        }

        @Override
        @Transactional
        public void markAsPublished(
                        String eventId) {

                repository.markAsPublished(eventId);
        }

        @Override
        @Transactional
        public void markAsFailed(
                        String eventId,
                        String error) {

                repository.markAsFailed(
                                eventId,
                                error);
        }

        @Override
        @Transactional
        public int recoverStaleEvents(
                        Instant threshold) {

                return repository.recoverStaleEvents(threshold);
        }
}