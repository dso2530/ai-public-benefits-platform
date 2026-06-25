package com.govtech.document.application.dto;

import java.util.Arrays;
import java.util.Objects;

public record DownloadedDocument(
        String fileName,
        String contentType,
        byte[] content
) {
       public DownloadedDocument {
        content = Arrays.copyOf(content, content.length);
    }

        @Override
        public boolean equals(final Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                final DownloadedDocument that = (DownloadedDocument) o;
                return Objects.equals(fileName, that.fileName)
                        && Objects.equals(contentType, that.contentType)
                        && Arrays.equals(content, that.content);
        }

        @Override
        public int hashCode() {
                int result = Objects.hash(fileName, contentType);
                result = 31 * result + Arrays.hashCode(content);
                return result;
        }

        @Override
        public String toString() {
                return "DownloadedDocument{"
                        + "fileName='" + fileName + '\''
                        + ", contentType='" + contentType + '\''
                        + ", content=" + Arrays.toString(content)
                        + '}';
        }
}