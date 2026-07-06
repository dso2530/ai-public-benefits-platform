package com.govtech.ocr.infrastructure.ocr;

import com.govtech.ocr.application.OCRProvider;
import com.govtech.ocr.infrastructure.ocr.client.PaddleOCRClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class PaddleOCRProvider implements OCRProvider {

    private final PaddleOCRClient paddleOCRClient;

    @Override
    public String extractText(InputStream inputStream) throws IOException {

        return paddleOCRClient.extractText(inputStream);

    }
}