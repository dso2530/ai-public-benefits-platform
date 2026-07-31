package com.govtech.connectors.common.downloader;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class DefaultChecksumService
        implements ChecksumService {

    @Override
    public String sha256(
            byte[] content)
            throws IOException {

        if (content == null) {

            throw new IOException(
                    "Cannot calculate checksum on null content");

        }

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(content);

            StringBuilder sb = new StringBuilder();

            for (byte b : hash) {

                sb.append(
                        String.format(
                                "%02x",
                                b & 0xff));

            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "SHA-256 algorithm not available",
                    e);

        }

    }

}