package com.govtech.platform.messaging.config;

import com.govtech.platform.messaging.dlt.DltTopicResolver;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;

import org.springframework.util.backoff.FixedBackOff;

@Configuration
@ConditionalOnProperty(prefix = "kafka", name = "enabled", havingValue = "true", matchIfMissing = false)
public class KafkaConsumerErrorConfig {

        @Bean
        public DefaultErrorHandler kafkaErrorHandler(
                        KafkaTemplate<String, Object> kafkaTemplate) {

                DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                                kafkaTemplate,
                                (record, exception) -> DltTopicResolver.resolve(
                                                record.topic(),
                                                record.partition()));

                return new DefaultErrorHandler(
                                recoverer,
                                new FixedBackOff(2_000L, 3L));
        }
}