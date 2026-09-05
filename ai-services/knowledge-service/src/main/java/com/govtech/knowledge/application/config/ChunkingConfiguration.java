package com.govtech.knowledge.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "knowledge.chunking")
public class ChunkingConfiguration {

    /**
     * Taille maximale d'un chunk en caractères.
     */
    private int size = 1000;

    /**
     * Nombre de caractères recopiés
     * entre deux chunks.
     */
    private int overlap = 200;

}