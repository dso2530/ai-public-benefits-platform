package com.govtech.connectors.common.downloader;

public interface ContentTypeDetector {

    String detect(byte[] content) throws Exception;
}