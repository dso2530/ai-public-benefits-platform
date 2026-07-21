package com.govtech.application.service.cerfa;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public final class PdfVisualComparator {

    private PdfVisualComparator() {
    }

    public static boolean assertSimilar(
            byte[] generated,
            byte[] reference)
            throws IOException {

        try (
                PDDocument doc1 = Loader.loadPDF(generated);
                PDDocument doc2 = Loader.loadPDF(reference)) {

            if (doc1.getNumberOfPages() != doc2.getNumberOfPages()) {
                return false;
            }

            PDFRenderer renderer1 = new PDFRenderer(doc1);

            PDFRenderer renderer2 = new PDFRenderer(doc2);

            for (int page = 0; page < doc1.getNumberOfPages(); page++) {

                BufferedImage image1 = renderer1.renderImageWithDPI(
                        page,
                        150);

                BufferedImage image2 = renderer2.renderImageWithDPI(
                        page,
                        150);

                if (!ImageComparator.assertImagesEqual(
                        image1,
                        image2, page)) {
                    return false;
                }
            }

            return true;
        }
    }
}