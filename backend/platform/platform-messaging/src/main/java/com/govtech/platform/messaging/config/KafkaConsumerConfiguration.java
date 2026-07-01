package com.govtech.platform.messaging.config;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
@ConditionalOnProperty(prefix = "kafka", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConsumerConfiguration {

        private final KafkaProperties properties;

        @Bean
        public ConsumerFactory<String, Object> consumerFactory() {

                Map<String, Object> config = new HashMap<>();

                config.put(
                                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                                properties.bootstrapServers());

                config.put(
                                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                                StringDeserializer.class);

                config.put(
                                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                                KafkaAvroDeserializer.class);

                config.put(
                                AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
                                properties.schemaRegistryUrl());

                config.put(
                                KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG,
                                true);

                config.put(
                                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                                "earliest");

                return new DefaultKafkaConsumerFactory<>(config);
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {

                ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();

                factory.setConsumerFactory(consumerFactory());

                return factory;
        }
}