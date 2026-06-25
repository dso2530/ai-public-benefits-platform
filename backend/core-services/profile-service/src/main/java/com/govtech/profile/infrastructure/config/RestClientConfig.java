/*package com.govtech.profile.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("http://api.example.com") // URL de base pour toutes les requêtes
                .requestFactory(new HttpComponentsClientHttpRequestFactory()) // Personnalisation de la requête HTTP
                .build();
    }
}*/
