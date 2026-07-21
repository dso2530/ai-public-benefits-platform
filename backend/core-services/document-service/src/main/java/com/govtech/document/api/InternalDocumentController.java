package com.govtech.document.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.govtech.document.api.dto.DocumentContractDto;
import com.govtech.document.application.usecase.DocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/documents")
@RequiredArgsConstructor
public class InternalDocumentController {

    private final DocumentService documentService;

    @GetMapping("/{subject}")
    public List<DocumentContractDto> findBySubject(
            @PathVariable String subject) {
        return documentService.findBySubject(subject);
    }
}