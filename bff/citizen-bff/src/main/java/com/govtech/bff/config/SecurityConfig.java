package com.govtech.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import com.govtech.bff.auth.handler.KeycloakSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        private final KeycloakSuccessHandler successHandler;    

        @Bean
        SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

                http
                        .cors(Customizer.withDefaults())
                        .csrf(csrf -> csrf.disable())
                        .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/actuator/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated())
                        .oauth2Login(oauth ->
                        oauth.successHandler(successHandler));
               

        return http.build();
    }

    
}