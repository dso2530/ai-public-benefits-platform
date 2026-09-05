package com.govtech.citizen.auth.oauth;

public record PkceChallenge(
        String codeVerifier,
        String codeChallenge) {
}