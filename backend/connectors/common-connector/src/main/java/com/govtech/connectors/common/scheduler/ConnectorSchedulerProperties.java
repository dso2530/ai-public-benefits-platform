package com.govtech.connectors.common.scheduler;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "connectors.scheduler")
public record ConnectorSchedulerProperties(

                boolean enabled,

                Duration initialDelay,

                Duration fixedDelay

) {

        public ConnectorSchedulerProperties {

                if (initialDelay == null) {
                        initialDelay = Duration.ofSeconds(10);
                }

                if (fixedDelay == null) {
                        fixedDelay = Duration.ofHours(1);
                }

        }

}