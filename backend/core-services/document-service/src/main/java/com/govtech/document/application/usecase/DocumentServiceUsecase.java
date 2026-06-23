package com.govtech.document.application.usecase;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.govtech.document.application.dto.DocumentDto;
import com.govtech.document.application.dto.DocumentSummaryDto;
import com.govtech.document.application.dto.DownloadedDocument;

import lombok.NonNull;

public interface DocumentServiceUsecase {
   

    List<DocumentDto> getDocuments(String subject);

    DocumentSummaryDto getSummary(
            String subject);

    DocumentDto upload(
            String subject,
            MultipartFile file,
            String type
    ) throws IOException;

    void delete(@NonNull
            Long id,
            String subject
    ) throws IOException;

    DownloadedDocument download(
            Long id,
            String subject
    )throws AccessDeniedException;
    
}
