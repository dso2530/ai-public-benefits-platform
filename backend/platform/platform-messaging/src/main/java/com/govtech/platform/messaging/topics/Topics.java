package com.govtech.platform.messaging.topics;

public final class Topics {

  private Topics() {
  }

  public static final String DOCUMENT_UPLOADED = "document.uploaded";

  public static final String DOCUMENT_SCAN_REQUESTED = "document.scan.requested";

  public static final String DOCUMENT_SCAN_COMPLETED = "document.scan.completed";

  public static final String DOCUMENT_USER_EXTRACTION_COMPLETED = "document.user.extraction.completed";

  public static final String DOCUMENT_EXTERNAL_EXTRACTION_COMPLETED = "document.external.extraction.completed";

  public static final String DOCUMENT_LLM_EXTRACTED = "document.llm.extracted";

  public static final String DOCUMENT_CLASSIFIED = "document.classified";

  public static final String DOCUMENT_EXTRACTION_COMPLETED = "document.extraction.completed";

}
