package com.govtech.application.service.cerfa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import com.govtech.application.service.cerfa.model.BoundingBox;
import com.govtech.application.service.cerfa.model.CerfaFieldKey;
import com.govtech.application.service.cerfa.model.CerfaTemplate;
import com.govtech.application.service.cerfa.model.FormField;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PdfCerfaWriter {

    public byte[] write(
            InputStream pdf,
            CerfaTemplate template,
            Map<CerfaFieldKey, String> values)
            throws IOException {

        try (
                InputStream input = pdf;
                PDDocument document = Loader.loadPDF(input.readAllBytes())) {

            PDType1Font font = new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA);

            for (FormField field : template.formFields()) {

                CerfaFieldKey key = field.fieldKey();

                if (key == null) {
                    continue;
                }

                String value = values.get(key);

                if (value == null || value.isBlank()) {
                    continue;
                }

                PDPage page = document.getPage(
                        field.pageNumber() - 1);

                BoundingBox box = field.entryBoundingBox();

                float fontSize = field.entryText() != null
                        ? field.entryText().fontSize()
                        : 10;

                try (PDPageContentStream cs = new PDPageContentStream(
                        document,
                        page,
                        PDPageContentStream.AppendMode.APPEND,
                        true)) {

                    cs.beginText();

                    cs.setFont(font, fontSize);

                    cs.newLineAtOffset(
                            box.x0(),
                            page.getMediaBox().getHeight()
                                    - box.y1());

                    cs.showText(value);

                    cs.endText();
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            document.save(out);

            return out.toByteArray();
        }
    }
}