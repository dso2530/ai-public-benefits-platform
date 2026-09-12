package com.govtech.extraction.application.port;

public interface ExtractionProvider {

    String extract(String prompt);

    String getModel();

}