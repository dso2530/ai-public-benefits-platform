package com.govtech.citizen.auth.controller;

import com.govtech.citizen.auth.dto.MobileLoginResponse;
import com.govtech.citizen.auth.dto.MobileTokenRequest;
import com.govtech.citizen.auth.dto.TokenResponse;
import com.govtech.citizen.auth.service.MobileOAuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/mobile")
@RequiredArgsConstructor
public class MobileAuthController {

    private final MobileOAuthService mobileOAuthService;

    /**
     * Démarre le login mobile PKCE
     */
    @GetMapping("/login")
    public ResponseEntity<MobileLoginResponse> login() {

        return ResponseEntity.ok(
                mobileOAuthService.createAuthorizationRequest());
    }

    /**
     * Echange le authorization_code reçu par l'application mobile
     */
    @PostMapping("/token")
    public ResponseEntity<TokenResponse> token(
            @RequestBody MobileTokenRequest request) {

        return ResponseEntity.ok(
                mobileOAuthService.exchange(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            Authentication authentication) {

        mobileOAuthService.logout(authentication);

        return ResponseEntity.noContent().build();
    }
}