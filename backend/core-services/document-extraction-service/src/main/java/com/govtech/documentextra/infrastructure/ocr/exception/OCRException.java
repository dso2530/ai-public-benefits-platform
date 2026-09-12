package com.govtech.documentextra.infrastructure.ocr.exception;

public class OCRException extends RuntimeException {

  public OCRException(String message) {
    super(message);
  }

  public OCRException(String message, Throwable cause) {
    super(message, cause);
  }
}
