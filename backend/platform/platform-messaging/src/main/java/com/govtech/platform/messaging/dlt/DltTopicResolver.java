package com.govtech.platform.messaging.dlt;

import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Component;

@Component
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

    public static boolean isDltTopic(String topic) {
        return topic != null && topic.endsWith(DLT_SUFFIX);
    }

    public static String resolveOriginalTopic(String dltTopic) {

        if (!isDltTopic(dltTopic)) {
            throw new IllegalArgumentException(
                    "Not a DLT topic: " + dltTopic);
        }

        return dltTopic.substring(
                0,
                dltTopic.length() - DLT_SUFFIX.length());
    }
}