package com.govtech.extraction.application;

public interface ExtractionProvider {

    String extract(String prompt);

    String getModel();

}