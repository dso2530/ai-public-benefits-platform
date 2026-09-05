package com.govtech.security.domain.port;

import com.govtech.security.domain.model.DocumentAnalysis;

public interface DocumentAnalyzerPort {

    DocumentAnalysis analyze(byte[] content);

}