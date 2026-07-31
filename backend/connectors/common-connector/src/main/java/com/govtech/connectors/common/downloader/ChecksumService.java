package com.govtech.connectors.common.downloader;

import java.io.IOException;

public interface ChecksumService {

    String sha256(
            byte[] content)
            throws IOException;

}