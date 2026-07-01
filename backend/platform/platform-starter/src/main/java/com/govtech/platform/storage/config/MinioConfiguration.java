package com.govtech.platform.storage.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

@Configuration
@ConditionalOnProperty(prefix = "minio", name = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(StorageProperties.class)
public class MinioConfiguration {

    @Bean
    public MinioClient minioClient(StorageProperties properties) {

        return MinioClient.builder()
                .endpoint(properties.endpoint())
                .credentials(
                        properties.accessKey(),
                        properties.secretKey())
                .build();
    }
}