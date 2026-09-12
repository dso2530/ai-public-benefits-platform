package com.govtech.consent.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.govtech.consent.api.dto.ConsentRequest;
import com.govtech.consent.api.dto.ConsentResponse;
import com.govtech.consent.application.usecase.ConsentUsecase;
import com.govtech.consent.domain.model.Consent;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/consents")
@RequiredArgsConstructor
public class ConsentController {

    private final ConsentUsecase service;

    @PostMapping
    public ResponseEntity<ConsentResponse> grant(
            @Valid @RequestBody ConsentRequest request) {

        Consent consent = service.grant(
                request.userId(),
                request.purpose(),
                request.version(),
                request.source());

        return ResponseEntity.ok(
                ConsentResponse.from(consent));
    }

    @GetMapping("/check")
    public boolean check(
            @RequestParam UUID userId,
            @RequestParam String purpose) {

        return service.hasConsent(
                userId,
                purpose);

    }

}