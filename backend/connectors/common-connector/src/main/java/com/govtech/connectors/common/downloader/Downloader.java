package com.govtech.connectors.common.downloader;

import java.io.IOException;
import java.net.URI;

import com.govtech.connectors.common.model.DownloadedResource;

public interface Downloader {

    DownloadedResource download(
            URI uri)
            throws IOException;

}