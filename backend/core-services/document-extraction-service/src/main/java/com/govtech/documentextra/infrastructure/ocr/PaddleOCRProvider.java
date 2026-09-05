package com.govtech.documentextra.infrastructure.ocr;

import com.govtech.documentextra.domain.port.OCRProvider;
import com.govtech.documentextra.infrastructure.ocr.client.PaddleOCRClient;
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