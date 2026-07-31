package com.govtech.connectors.common.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(ConnectorSchedulerProperties.class)
@ConditionalOnProperty(prefix = "connectors.scheduler", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SchedulingConfiguration {

}