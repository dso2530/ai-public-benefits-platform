package  com.govtech.document.application.usecase;
                
import java.util.List;

import org.springframework.stereotype.Service;

import com.govtech.document.application.dto.DocumentDto;
import com.govtech.document.application.dto.DocumentSummaryDto;
import com.govtech.document.infrastructure.persistence.DocumentJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentJpaRepository repository;

    public List<DocumentDto> getDocuments(
            String subject) {

        return repository
                .findBySubjectOrderByUploadedAtDesc(subject)
                .stream()
                .map(doc -> new DocumentDto(
                        doc.getId(),
                        doc.getName(),
                        doc.getType(),
                        doc.getStatus(),
                        doc.getUploadedAt()))
                .toList();
    }

    public DocumentSummaryDto getSummary(
            String subject) {

        long total =
                repository.countBySubject(subject);

        long validated =
                repository.countBySubjectAndStatus(
                        subject,
                        "VALIDATED");

        return new DocumentSummaryDto(
                (int) total,
                (int) validated,
                (int) (total - validated)
        );
    }
}