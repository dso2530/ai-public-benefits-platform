package com.govtech.application.service.usecase;

import com.govtech.application.domain.model.ApplicationStatus;
import com.govtech.application.domain.model.DocumentType;
import com.govtech.application.domain.model.PackageStatus;
import com.govtech.application.service.dto.PackageDocument;
import com.govtech.application.service.dto.PackageResult;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class PackageBuilder {

    private static final String CERFA_FILENAME = "cerfa-10840-07.pdf";

    public PackageResult build(
            byte[] cerfa,
            List<PackageDocument> documents,
            List<DocumentType> missingDocuments) {

        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zip = new ZipOutputStream(baos)) {

            add(zip, CERFA_FILENAME, cerfa);

            for (PackageDocument document : documents) {
                add(zip, document.fileName(), document.content());
            }

            zip.finish();

            return new PackageResult(
                    baos.toByteArray(),
                    PackageStatus.GENERATED,
                    !missingDocuments.isEmpty()
                            ? ApplicationStatus.READY_TO_COMPLETE
                            : ApplicationStatus.READY_TO_SUBMIT);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to build application package",
                    e);
        }
    }

    private void add(
            ZipOutputStream zip,
            String fileName,
            byte[] content) throws IOException {

        zip.putNextEntry(new ZipEntry(fileName));
        zip.write(content);
        zip.closeEntry();
    }
}