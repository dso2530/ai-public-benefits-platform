package com.govtech.security.domain.port;

public interface DocumentAnalyzerPort {

    DocumentAnalysis analyze(byte[] content);

}