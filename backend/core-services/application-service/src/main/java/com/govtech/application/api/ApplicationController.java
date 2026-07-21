package com.govtech.application.api;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.govtech.application.api.dto.ApplicationDto;
import com.govtech.application.service.usecase.ApplicationService;
import com.govtech.platform.storage.dto.DownloadedDocument;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping
    public List<ApplicationDto> findBySubject(@AuthenticationPrincipal Jwt jwt) {
        return applicationService.findBySubject(jwt.getSubject());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDto> findById(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        return applicationService.findById(id, jwt.getSubject())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Void> submit(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        applicationService.submit(id, jwt.getSubject());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/document")
    public ResponseEntity<byte[]> download(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) throws AccessDeniedException {

        DownloadedDocument document = applicationService.downloadPackage(id, jwt.getSubject());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"application-package.zip\"")
                .body(document.content());
    }
}