package com.govtech.citizen.auth.oauth;

import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

@Component
public class OAuthStateStore {

    private final Cache<String, String> store;

    public OAuthStateStore() {
        this.store = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(5))
                .maximumSize(10_000)
                .build();
    }

    public void save(String state, String codeVerifier) {
        store.put(state, codeVerifier);
    }

    public String get(String state) {
        return store.getIfPresent(state);
    }

    public void remove(String state) {
        store.invalidate(state);
    }
}