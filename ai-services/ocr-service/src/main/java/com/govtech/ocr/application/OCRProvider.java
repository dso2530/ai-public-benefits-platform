package com.govtech.ocr.application;

import java.io.IOException;
import java.io.InputStream;

public interface OCRProvider {

    String extractText(InputStream inputStream) throws IOException;

}