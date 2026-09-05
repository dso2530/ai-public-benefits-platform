package com.govtech.platform.document.tika;

import org.apache.tika.Tika;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class TikaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Tika tika() {

        return new Tika();

    }

}