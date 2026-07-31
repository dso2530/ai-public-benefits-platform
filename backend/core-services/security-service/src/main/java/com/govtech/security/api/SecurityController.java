package com.govtech.security.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.govtech.security.api.dto.ScanDocumentRequest;
import com.govtech.security.api.dto.SecurityScanResponse;
import com.govtech.security.application.usecase.ScanDocumentUseCase;
import com.govtech.security.domain.model.SecurityScan;
import com.govtech.security.infrastructure.persistence.SecurityScanJpaAdapter;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class SecurityController {

    private final ScanDocumentUseCase scanDocumentUseCase;

    private final SecurityScanJpaAdapter repository;

    /**
     * Déclenche un scan manuel
     */
    @PostMapping("/documents/{documentId}/scan")
    public ResponseEntity<SecurityScan> scan(
            @PathVariable Long documentId,
            @RequestBody ScanDocumentRequest request) {

        SecurityScan result = scanDocumentUseCase.execute(

                documentId,

                request.content(),

                request.sha256()

        );

        return ResponseEntity.ok(result);

    }

    /**
     * Récupère le dernier résultat de scan
     */
    @GetMapping("/documents/{documentId}")
    public ResponseEntity<SecurityScanResponse> status(
            @PathVariable Long documentId) {

        return repository
                .findLatestByDocumentId(documentId)

                .map(scan -> ResponseEntity.ok(
                        SecurityScanResponse.from(scan)))

                .orElse(
                        ResponseEntity.notFound().build());

    }

}