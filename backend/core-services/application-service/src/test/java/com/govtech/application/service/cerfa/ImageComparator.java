package com.govtech.application.service.cerfa;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.pdmodel.PDDocument;

public final class ImageComparator {

    private ImageComparator() {
    }

    public static boolean assertSimilar(
            Path expected,
            Path actual)
            throws IOException {

        try (
                PDDocument expectedDoc = Loader.loadPDF(expected.toFile());
                PDDocument actualDoc = Loader.loadPDF(actual.toFile())) {

            if (expectedDoc.getNumberOfPages()
                    != actualDoc.getNumberOfPages()) {
                return false;
            }

            PDFRenderer expectedRenderer =
                    new PDFRenderer(expectedDoc);

            PDFRenderer actualRenderer =
                    new PDFRenderer(actualDoc);

            for (int page = 0;
                    page < expectedDoc.getNumberOfPages();
                    page++) {

                BufferedImage image1 =
                        expectedRenderer.renderImageWithDPI(page, 150);

                BufferedImage image2 =
                        actualRenderer.renderImageWithDPI(page, 150);

                if (!assertImagesEqual(image1, image2, page)) {
                    return false;
                }
            }

            return true;
        }
    }


    public static boolean assertImagesEqual(
            BufferedImage expected,
            BufferedImage actual,
            int page) {

        if (expected.getWidth() != actual.getWidth()
                || expected.getHeight() != actual.getHeight()) {
            return false;
        }


        long differentPixels = 0;

        int width = expected.getWidth();
        int height = expected.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if (expected.getRGB(x, y)
                        != actual.getRGB(x, y)) {

                    differentPixels++;
                }
            }
        }


        double difference =
                differentPixels * 100.0
                        / (width * height);


        return difference < 0.5;
    }
}