package com.govtech.documentextra.domain.port;

import java.io.IOException;
import java.io.InputStream;

public interface OCRProvider {

    String extractText(InputStream inputStream) throws IOException;

}