package com.govtech.platform.database.outbox;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OutboxEventJpaRepository
                extends JpaRepository<OutboxEventJpaEntity, UUID> {

        /**
         * Sélectionne les événements prêts à être publiés.
         *
         * SKIP LOCKED permet à plusieurs publishers de travailler
         * en concurrence sans sélectionner les mêmes lignes.
         */
        @Query(value = """
                        SELECT *
                        FROM outbox_event
                        WHERE status = 'READY_TO_PUBLISH'
                        ORDER BY created_at
                        LIMIT :batchSize
                        FOR UPDATE SKIP LOCKED
                        """, nativeQuery = true)
        List<OutboxEventJpaEntity> claimReadyToPublish(
                        @Param("batchSize") int batchSize);

        /**
         * Passe l'événement à PUBLISHING
         * et mémorise le moment où il a été claimé.
         */
        @Modifying
        @Query("""
                        UPDATE OutboxEventJpaEntity e
                           SET e.status = 'PUBLISHING',
                               e.claimedAt = CURRENT_TIMESTAMP
                         WHERE e.eventId = :eventId
                        """)
        int markAsPublishing(
                        @Param("eventId") String eventId);

        /**
         * Publication Kafka réussie.
         */
        @Modifying
        @Query("""
                        UPDATE OutboxEventJpaEntity e
                           SET e.status = 'MARKED_AS_PUBLISHED',
                               e.publishedAt = CURRENT_TIMESTAMP,
                               e.claimedAt = null,
                               e.lastError = null
                         WHERE e.eventId = :eventId
                        """)
        int markAsPublished(
                        @Param("eventId") String eventId);

        /**
         * Échec de publication.
         *
         * L'événement redevient publiable.
         */
        @Modifying
        @Query("""
                        UPDATE OutboxEventJpaEntity e
                           SET e.status = 'READY_TO_PUBLISH',
                               e.attempts = e.attempts + 1,
                               e.claimedAt = null,
                               e.lastError = :error
                         WHERE e.eventId = :eventId
                        """)
        int markAsFailed(
                        @Param("eventId") String eventId,
                        @Param("error") String error);

        /**
         * Récupère les événements restés en PUBLISHING
         * après un crash ou un arrêt du publisher.
         */
        @Modifying
        @Query("""
                        UPDATE OutboxEventJpaEntity e
                           SET e.status = 'READY_TO_PUBLISH',
                               e.attempts = e.attempts + 1,
                               e.claimedAt = null
                         WHERE e.status = 'PUBLISHING'
                           AND e.claimedAt < :threshold
                        """)
        int recoverStaleEvents(
                        @Param("threshold") Instant threshold);
}