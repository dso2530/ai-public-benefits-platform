package com.govtech.connectors.common.scheduler;

import java.time.Duration;

public record PollingPolicy(

        Duration initialDelay,

        Duration delay

) {

    public static PollingPolicy hourly() {

        return new PollingPolicy(

                Duration.ofSeconds(10),

                Duration.ofHours(1)

        );

    }

}