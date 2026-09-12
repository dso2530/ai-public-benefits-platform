package com.govtech.platform.messaging.dlt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;

import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class DltReplayService {

    private final ConsumerFactory<String, Object> consumerFactory;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final DltReplayProperties properties;
    private final DltTopicResolver topicResolver;

    public int replay() {

        String groupId = resolveConsumerGroup();

        log.info(
                "Starting DLT replay: groupId={}, pattern={}, batchSize={}",
                groupId,
                properties.getTopicPattern(),
                properties.getBatchSize());

        String clientId = "dlt-replay-" + groupId;

        try (Consumer<String, Object> consumer = consumerFactory.createConsumer(groupId, clientId)) {

            consumer.subscribe(
                    java.util.regex.Pattern.compile(
                            properties.getTopicPattern()));

            ConsumerRecords<String, Object> records = consumer.poll(Duration.ofSeconds(5));

            if (records.isEmpty()) {
                log.debug("No DLT messages available for replay");
                return 0;
            }

            int replayed = 0;

            for (ConsumerRecord<String, Object> record : records) {

                if (replayed >= properties.getBatchSize()) {
                    break;
                }

                boolean success = replay(
                        consumer,
                        record);

                if (success) {
                    replayed++;
                } else {
                    break;
                }
            }

            log.info(
                    "DLT replay completed: replayed={}",
                    replayed);

            return replayed;

        } catch (Exception e) {

            log.error(
                    "Unexpected error during DLT replay",
                    e);

            return 0;
        }
    }

    private boolean replay(
            Consumer<String, Object> consumer,
            ConsumerRecord<String, Object> record) {

        String dltTopic = record.topic();

        String originalTopic = topicResolver.resolveOriginalTopic(dltTopic);

        log.info(
                "Replaying DLT message: dltTopic={}, originalTopic={}, partition={}, offset={}",
                dltTopic,
                originalTopic,
                record.partition(),
                record.offset());

        try {

            var producerRecord = new org.apache.kafka.clients.producer.ProducerRecord<>(
                    originalTopic,
                    record.partition(),
                    record.key(),
                    record.value());

            copyHeaders(record, producerRecord);

            kafkaTemplate
                    .send(producerRecord)
                    .get();

            commit(
                    consumer,
                    record);

            log.info(
                    "DLT message successfully replayed: dltTopic={}, originalTopic={}, partition={}, offset={}",
                    dltTopic,
                    originalTopic,
                    record.partition(),
                    record.offset());

            return true;

        } catch (Exception e) {

            log.error(
                    "Failed to replay DLT message: dltTopic={}, partition={}, offset={}",
                    dltTopic,
                    record.partition(),
                    record.offset(),
                    e);

            return false;
        }
    }

    private void commit(
            Consumer<String, Object> consumer,
            ConsumerRecord<String, Object> record) {

        TopicPartition topicPartition = new TopicPartition(
                record.topic(),
                record.partition());

        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

        offsets.put(
                topicPartition,
                new OffsetAndMetadata(record.offset() + 1));

        consumer.commitSync(offsets);
    }

    private void copyHeaders(
            ConsumerRecord<String, Object> source,
            org.apache.kafka.clients.producer.ProducerRecord<String, Object> target) {

        for (Header header : source.headers()) {

            String name = header.key();

            /*
             * Do not propagate internal Spring Kafka headers.
             */
            if (KafkaHeaders.RECEIVED_TOPIC.equals(name)
                    || KafkaHeaders.RECEIVED_PARTITION.equals(name)
                    || KafkaHeaders.OFFSET.equals(name)
                    || KafkaHeaders.GROUP_ID.equals(name)) {
                continue;
            }

            target.headers().add(
                    name,
                    header.value());
        }
    }

    private String resolveConsumerGroup() {

        if (properties.getConsumerGroup() != null
                && !properties.getConsumerGroup().isBlank()) {

            return properties.getConsumerGroup();
        }

        return "dlt-replay";
    }
}