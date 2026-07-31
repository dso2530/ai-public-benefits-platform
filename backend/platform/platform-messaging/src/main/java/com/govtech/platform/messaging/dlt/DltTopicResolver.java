package com.govtech.platform.messaging.dlt;

import org.apache.kafka.common.TopicPartition;

public final class DltTopicResolver {

    private static final String DLT_SUFFIX = ".DLT";

    private DltTopicResolver() {
    }

    public static TopicPartition resolve(
            String topic,
            int partition) {

        return new TopicPartition(
                topic + DLT_SUFFIX,
                partition);

    }

    public static String resolve(
            String topic) {

        return topic + DLT_SUFFIX;

    }

}