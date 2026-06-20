
package  com.govtech.document.api;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.govtech.document.application.dto.DocumentDto;
import com.govtech.document.application.dto.DocumentSummaryDto;
import com.govtech.document.application.usecase.DocumentService;

import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.web.bind.annotation.RequestMapping;


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
}