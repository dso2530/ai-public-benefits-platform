package com.govtech.knowledge.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "knowledge")
public class KnowledgeProperties {

    private Chunking chunking = new Chunking();

    @Getter
    @Setter
    public static class Chunking {

        private int size = 1000;

        private int overlap = 200;

    }

}