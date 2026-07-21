package com.govtech.document.application.usecase;

import java.util.List;

import com.govtech.document.api.dto.DocumentContractDto;

public interface InternalDocumentServiceUseCase {
    public List<DocumentContractDto> findBySubject(String subject);

}
