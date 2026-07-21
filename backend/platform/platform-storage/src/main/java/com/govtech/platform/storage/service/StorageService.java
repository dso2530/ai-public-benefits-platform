package com.govtech.platform.storage.service;

import io.minio.Result;
import io.minio.messages.Item;
import java.io.InputStream;

public interface StorageService {

  String upload(InputStream inputStream, long size, String contentType, String bucket, String objectKey);

  InputStream download(String bucket, String objectKey);

  void delete(String bucket, String objectKey);

  boolean exists(String bucket, String objectKey);

  Iterable<Result<Item>> list(String bucket, String prefix);
}
