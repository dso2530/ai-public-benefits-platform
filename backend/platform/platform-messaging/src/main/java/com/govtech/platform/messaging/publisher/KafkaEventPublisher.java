package com.govtech.platform.messaging.publisher;

import java.util.concurrent.CompletableFuture;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.govtech.platform.messaging.exception.MessagingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "kafka", name = "enabled", havingValue = "true")
public class KafkaEventPublisher
    implements EventPublisher {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public CompletableFuture<Void> publish(
      String topic,
      String key,
      Object event) {

    return kafkaTemplate
        .send(topic, key, event)

        .thenAccept(result -> {

          log.info(
              "Published event {} on topic {}",
              event.getClass().getSimpleName(),
              topic);

        })

        .exceptionally(ex -> {

          log.error(
              "Kafka publish failed topic={}",
              topic,
              ex);

          throw new MessagingException(
              "Kafka publish failed topic=" + topic,
              ex);

        });

  }

}