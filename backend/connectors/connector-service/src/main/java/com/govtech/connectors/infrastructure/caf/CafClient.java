package com.govtech.connectors.infrastructure.caf;

import java.net.URI;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CafClient {

        private final CafProperties properties;

        public List<URI> documents() {

                if (!properties.enabled()) {
                        return List.of();
                }

                return properties.documents()
                                .stream()
                                .filter(url -> url != null && !url.isBlank())
                                .map(URI::create)
                                .toList();
        }
}