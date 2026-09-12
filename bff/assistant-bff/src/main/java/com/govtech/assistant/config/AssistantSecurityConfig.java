package com.govtech.assistant.config;

import com.govtech.bff.security.auth.handler.KeycloakSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class AssistantSecurityConfig {

    private final KeycloakSuccessHandler successHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        return http

                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/actuator/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/auth/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())

                .oauth2Login(
                        oauth -> oauth.successHandler(successHandler))

                .build();
    }
}