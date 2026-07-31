package com.govtech.connectors.infrastructure.redis;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.govtech.connectors.common.model.ConnectorDocument;
import com.govtech.connectors.domain.port.DocumentRegistry;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisDocumentRegistry
        implements DocumentRegistry {

    private static final String PREFIX = "connector:document:";

    private final StringRedisTemplate redis;

    @Override
    public boolean exists(
            String checksum) {

        return Boolean.TRUE.equals(
                redis.hasKey(
                        PREFIX + checksum));

    }

    @Override
    public void save(
            ConnectorDocument document) {

        if (document.checksum() == null) {

            return;

        }

        redis.opsForValue()
                .set(
                        PREFIX + document.checksum(),
                        document.id(),
                        Duration.ofDays(365));

    }

    @Override
    public Optional<ConnectorDocument> findByChecksum(
            String checksum) {

        return Optional.empty();

    }

}