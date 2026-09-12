package com.govtech.platform.messaging.config;

import com.govtech.platform.messaging.dlt.DltReplayProperties;
import com.govtech.platform.messaging.dlt.DltReplayScheduler;
import com.govtech.platform.messaging.dlt.DltReplayService;
import com.govtech.platform.messaging.dlt.DltTopicResolver;
import com.govtech.platform.messaging.idempotency.IdempotencyProperties;
import com.govtech.platform.messaging.outbox.OutboxProperties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties({
                OutboxProperties.class,
                IdempotencyProperties.class,
                DltReplayProperties.class
})
public class MessagingConfiguration {

        @Bean
        @ConditionalOnProperty(prefix = "messaging.kafka.dlt.replay", name = "enabled", havingValue = "true", matchIfMissing = false)
        public DltReplayService dltReplayService(
                        ConsumerFactory<String, Object> consumerFactory,
                        KafkaTemplate<String, Object> kafkaTemplate,
                        DltReplayProperties properties,
                        DltTopicResolver topicResolver) {

                return new DltReplayService(
                                consumerFactory,
                                kafkaTemplate,
                                properties,
                                topicResolver);
        }

        @Bean
        @ConditionalOnProperty(prefix = "messaging.kafka.dlt.replay", name = "enabled", havingValue = "true", matchIfMissing = false)
        public DltReplayScheduler dltReplayScheduler(
                        DltReplayService replayService) {

                return new DltReplayScheduler(replayService);
        }
}