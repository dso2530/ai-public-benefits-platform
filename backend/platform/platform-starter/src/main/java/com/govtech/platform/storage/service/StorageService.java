package com.govtech.platform.storage.service;

import io.minio.Result;
import io.minio.messages.Item;
import java.io.InputStream;

public interface StorageService {

  String upload(InputStream inputStream, long size, String contentType, String objectKey);

  InputStream download(String objectKey);

  void delete(String objectKey);

  boolean exists(String objectKey);

  Iterable<Result<Item>> list(String prefix);
}
