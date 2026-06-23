
package  com.govtech.document.api;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.govtech.document.application.dto.DocumentDto;
import com.govtech.document.application.dto.DocumentSummaryDto;
import com.govtech.document.application.dto.DownloadedDocument;
import com.govtech.document.application.usecase.DocumentService;

import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    public List<DocumentDto> getDocuments(
            @AuthenticationPrincipal Jwt jwt) {

        return documentService.getDocuments(
                jwt.getSubject());
    }

    @GetMapping("/summary")
    public DocumentSummaryDto getSummary(
            @AuthenticationPrincipal Jwt jwt) {

        return documentService.getSummary(
                jwt.getSubject());
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public DocumentDto upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") String type,
            @AuthenticationPrincipal Jwt jwt)
            throws IOException {

        return documentService.upload(
                jwt.getSubject(),
                file,
                type
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal Jwt jwt)
            throws IOException {

        documentService.delete(
                id,
                jwt.getSubject()
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal Jwt jwt) throws AccessDeniedException {

        DownloadedDocument document =
                documentService.download(
                        id,
                        jwt.getSubject()
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                document.fileName() + "\""
                )
                .contentType(
                        MediaType.parseMediaType(
                                document.contentType()
                        )
                )
                .body(
                        new ByteArrayResource(
                                document.content()
                        )
                );
    }
}