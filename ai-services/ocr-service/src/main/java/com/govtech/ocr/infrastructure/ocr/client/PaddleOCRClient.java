package com.govtech.ocr.infrastructure.ocr.client;

import com.govtech.ocr.infrastructure.ocr.dto.OCRResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;


@Component
@RequiredArgsConstructor
public class PaddleOCRClient {

    private final RestClient paddleOcrRestClient;

    public String extractText(InputStream inputStream) throws IOException {

        byte[] bytes = inputStream.readAllBytes();       

        ByteArrayResource resource = new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return "document.pdf";
            }
        };

     

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);

        OCRResponse response = paddleOcrRestClient.post()
                .uri("/ocr")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(OCRResponse.class);

        return response != null ? response.text() : "";
    }

}