package com.govtech.application.service.usecase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.springframework.stereotype.Service;

import com.govtech.application.domain.model.CerfaModel;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class PdfGenerationService {

    public byte[] generate(CerfaModel model) {

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            Font title = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font section = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);

            document.add(new Paragraph("CERFA - " + model.aidCode(), title));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Applicant", section));
            document.add(new Paragraph("First name: " + model.firstName()));
            document.add(new Paragraph("Last name: " + model.lastName()));
            document.add(new Paragraph("Birth date: " + model.birthDate()));
            document.add(new Paragraph("Nationality: " + model.nationality()));
            document.add(new Paragraph("Email: " + model.email()));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Address", section));
            document.add(new Paragraph("Postal code: " + model.postalCode()));
            document.add(new Paragraph("City: " + model.city()));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Household", section));
            document.add(new Paragraph("Housing status: " + model.housingStatus()));
            document.add(new Paragraph("Children: " + model.childrenCount()));
            document.add(new Paragraph("Single parent: " + model.singleParent()));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Income", section));
            document.add(new Paragraph("Reference income: " + model.referenceIncome()));
            document.add(new Paragraph("Tax year: " + model.taxYear()));
            document.add(new Paragraph("Assessment year: " + model.assessmentYear()));

            document.close();

            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to generate CERFA PDF", e);
        }
    }
}