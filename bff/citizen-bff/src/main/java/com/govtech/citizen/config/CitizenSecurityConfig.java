package com.govtech.citizen.config;

import com.govtech.bff.security.auth.handler.KeycloakSuccessHandler;
import com.govtech.citizen.filter.InternalJwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@RequiredArgsConstructor
public class CitizenSecurityConfig {

  private final KeycloakSuccessHandler successHandler;

  private final InternalJwtAuthenticationFilter internalJwtAuthenticationFilter;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) {

    CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    repository.setHeaderName("X-CSRF-TOKEN");

    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    requestHandler.setCsrfRequestAttributeName("_csrf");

    http.cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth -> auth.requestMatchers(
                "/api/auth/**", "/actuator/**", "/swagger-ui/**", "/v3/api-docs/**", "/api/dashboard",
                "/api/assistant/**")
                .permitAll()
                .anyRequest()
                .authenticated())
        .addFilterBefore(internalJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

        .oauth2Login(oauth -> oauth.successHandler(successHandler));

    return http.build();
  }
}