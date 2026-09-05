package com.govtech.platform.storage.service;

import java.io.InputStream;
import java.util.List;

public interface StorageService {

  String upload(InputStream inputStream, long size, String contentType, String bucket, String objectKey);

  InputStream download(String bucket, String objectKey);

  void delete(String bucket, String objectKey);

  boolean exists(String bucket, String objectKey);

  List<String> list(
      String bucket,
      String prefix);

  void move(
      String sourceBucket,
      String sourceObjectKey,
      String targetBucket,
      String targetObjectKey);

}
