package com.govtech.platform.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnBean(KafkaTemplate.class)
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(
            String topic,
            String key,
            Object event) {

        kafkaTemplate.send(topic, key, event)
                .whenComplete((result, ex) -> {

                    if (ex != null) {
                        log.error(
                                "Failed to publish event on topic {}",
                                topic,
                                ex);
                    } else {
                        log.info(
                                "Published event {} on topic {}",
                                event.getClass().getSimpleName(),
                                topic);
                    }

                });
    }
}