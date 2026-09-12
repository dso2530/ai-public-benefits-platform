package com.govtech.platform.messaging.dlt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "messaging.kafka.dlt.replay")
public class DltReplayProperties {

    /**
     * Enables automatic DLT replay.
     */
    private boolean enabled = false;

    /**
     * Delay between two replay executions.
     */
    private long fixedDelay = 30_000L;

    /**
     * Maximum number of messages replayed per execution.
     */
    private int batchSize = 100;

    /**
     * Kafka consumer group dedicated to DLT replay.
     *
     * If empty, the application name is used.
     */
    private String consumerGroup;

    /**
     * Only topics matching this regex are replayed.
     */
    private String topicPattern = ".*\\.DLT";

    /**
     * Maximum number of replay attempts.
     */
    private int maxReplays = 3;
}