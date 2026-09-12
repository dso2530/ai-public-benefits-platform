package com.govtech.connectors.infrastructure.redis;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.govtech.connectors.common.model.RegisteredDocument;
import com.govtech.connectors.domain.port.DocumentRegistry;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisDocumentRegistry
        implements DocumentRegistry {

    private static final String CHECKSUM_PREFIX = "connector:document:checksum:";

    private static final String EXTERNAL_ID_PREFIX = "connector:document:id:";

    private final StringRedisTemplate redis;

    @Override
    public boolean existsByExternalId(
            String externalId) {

        if (externalId == null) {
            return false;
        }

        return Boolean.TRUE.equals(
                redis.hasKey(
                        EXTERNAL_ID_PREFIX + externalId));

    }

    @Override
    public boolean existsByChecksum(
            String checksum) {

        if (checksum == null) {
            return false;
        }

        return Boolean.TRUE.equals(
                redis.hasKey(
                        CHECKSUM_PREFIX + checksum));

    }

    @Override
    public void save(
            String externalId,
            String checksum,
            String source) {

        if (externalId != null) {

            redis.opsForValue().set(
                    EXTERNAL_ID_PREFIX + externalId,
                    source,
                    Duration.ofDays(365));

        }

        if (checksum != null) {

            redis.opsForValue().set(
                    CHECKSUM_PREFIX + checksum,
                    externalId,
                    Duration.ofDays(365));

        }

    }

    @Override
    public Optional<RegisteredDocument> findByChecksum(
            String checksum) {

        if (checksum == null) {
            return Optional.empty();
        }

        String externalId = redis.opsForValue()
                .get(
                        CHECKSUM_PREFIX + checksum);

        if (externalId == null) {
            return Optional.empty();
        }

        return Optional.of(
                new RegisteredDocument(
                        externalId,
                        checksum,
                        null));

    }
}