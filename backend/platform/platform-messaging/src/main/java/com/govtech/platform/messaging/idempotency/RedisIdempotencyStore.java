package com.govtech.platform.messaging.idempotency;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisIdempotencyStore
        implements IdempotencyStore {

    private static final String PREFIX = "messaging:idempotency:";

    private final StringRedisTemplate redisTemplate;
    private final IdempotencyProperties properties;

    @Override
    public boolean tryAcquire(
            String consumerGroup,
            String eventId) {

        if (!properties.enabled()) {
            return true;
        }

        String key = key(consumerGroup, eventId);

        Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(
                        key,
                        IdempotencyStatus.PROCESSING.name(),
                        properties.ttl());

        return Boolean.TRUE.equals(acquired);
    }

    @Override
    public void markProcessed(
            String consumerGroup,
            String eventId) {

        redisTemplate.opsForValue()
                .set(
                        key(consumerGroup, eventId),
                        IdempotencyStatus.PROCESSED.name(),
                        properties.ttl());
    }

    @Override
    public void release(
            String consumerGroup,
            String eventId) {

        redisTemplate.delete(
                key(consumerGroup, eventId));
    }

    private String key(
            String consumerGroup,
            String eventId) {

        return PREFIX
                + consumerGroup
                + ":"
                + eventId;
    }
}