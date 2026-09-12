package com.govtech.platform.database.outbox;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "outbox_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEventJpaEntity {

    @Id
    private UUID id;

    @Column(name = "event_id", nullable = false, unique = true, length = 100)
    private String eventId;

    @Column(name = "event_type", nullable = false, length = 255)
    private String eventType;

    @Column(name = "topic", nullable = false, length = 255)
    private String topic;

    @Column(name = "aggregate_type", length = 255)
    private String aggregateType;

    @Column(name = "aggregate_id", length = 255)
    private String aggregateId;

    /**
     * Payload Avro déjà sérialisé.
     */
    @Column(name = "payload", nullable = false, columnDefinition = "BYTEA")
    private byte[] payload;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private OutboxStatusJpaEntity status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * Date à laquelle l'événement a été pris
     * en charge par un publisher.
     */
    @Column(name = "claimed_at")
    private Instant claimedAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "attempts", nullable = false)
    private int attempts;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    public static OutboxEventJpaEntity create(
            String eventId,
            String eventType,
            String topic,
            String aggregateType,
            String aggregateId,
            byte[] payload) {

        OutboxEventJpaEntity entity = new OutboxEventJpaEntity();

        entity.id = UUID.randomUUID();
        entity.eventId = eventId;
        entity.eventType = eventType;
        entity.topic = topic;
        entity.aggregateType = aggregateType;
        entity.aggregateId = aggregateId;
        entity.payload = payload;
        entity.status = OutboxStatusJpaEntity.READY_TO_PUBLISH;
        entity.createdAt = Instant.now();
        entity.attempts = 0;

        return entity;
    }

    public void markAsPublishing() {

        this.status = OutboxStatusJpaEntity.PUBLISHING;

        this.claimedAt = Instant.now();
    }

    public void markAsPublished() {

        this.status = OutboxStatusJpaEntity.MARKED_AS_PUBLISHED;

        this.publishedAt = Instant.now();
        this.claimedAt = null;
        this.lastError = null;
    }

    public void markAsFailed(String error) {

        this.status = OutboxStatusJpaEntity.READY_TO_PUBLISH;

        this.attempts++;
        this.claimedAt = null;
        this.lastError = error;
    }
}