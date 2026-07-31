package com.govtech.security.infrastructure.scanner;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClamAvClient {

        private static final int CHUNK_SIZE = 2048;

        private final ClamAvProperties properties;

        public boolean scan(byte[] content) {

                log.info(
                                "Starting ClamAV scan host={} port={} size={} bytes",
                                properties.host(),
                                properties.port(),
                                content != null ? content.length : null);

                if (content == null || content.length == 0) {
                        log.warn("Empty content sent to ClamAV");
                        return false;
                }

                try (
                                Socket socket = new Socket(properties.host(), properties.port());
                                OutputStream out = socket.getOutputStream();
                                InputStream in = socket.getInputStream()) {

                        // Commande ClamAV
                        out.write("zINSTREAM\0".getBytes(StandardCharsets.US_ASCII));

                        int offset = 0;

                        while (offset < content.length) {

                                int chunkSize = Math.min(CHUNK_SIZE, content.length - offset);

                                out.write(new byte[] {
                                                (byte) (chunkSize >> 24),
                                                (byte) (chunkSize >> 16),
                                                (byte) (chunkSize >> 8),
                                                (byte) chunkSize
                                });

                                out.write(content, offset, chunkSize);

                                offset += chunkSize;
                        }

                        // Fin du stream
                        out.write(new byte[] { 0, 0, 0, 0 });
                        out.flush();

                        ByteArrayOutputStream responseBuffer = new ByteArrayOutputStream();

                        int b;
                        while ((b = in.read()) != -1 && b != 0) {
                                responseBuffer.write(b);
                        }

                        String response = responseBuffer.toString(StandardCharsets.US_ASCII);

                        log.info("ClamAV response=[{}]", response);

                        boolean infected = response.contains("FOUND");

                        log.info("ClamAV detected malware={}", infected);

                        return infected;

                } catch (Exception e) {

                        log.error("ClamAV communication error", e);

                        throw new RuntimeException("ClamAV unavailable", e);
                }
        }
}